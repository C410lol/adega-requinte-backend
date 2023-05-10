package com.adega.ms.user.adegauserservice.controllers;

import com.adega.ms.user.adegauserservice.dtos.MailDto;
import com.adega.ms.user.adegauserservice.dtos.UserDto;
import com.adega.ms.user.adegauserservice.dtos.UserLoginDto;
import com.adega.ms.user.adegauserservice.dtos.UserMailDto;
import com.adega.ms.user.adegauserservice.enums.UserRole;
import com.adega.ms.user.adegauserservice.enums.UserStatus;
import com.adega.ms.user.adegauserservice.models.AuthModel;
import com.adega.ms.user.adegauserservice.models.UserModel;
import com.adega.ms.user.adegauserservice.services.JwtService;
import com.adega.ms.user.adegauserservice.services.MailService;
import com.adega.ms.user.adegauserservice.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final LocalDateTime localDateTime;
    private final MailService mailService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody @Valid @NotNull UserDto userDto)
            throws JsonProcessingException {
        if(userService.emailAlreadyExists(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists!");
        }
        if(userService.cpfAlreadyExists(userDto.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF already exists!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setRole(UserRole.ROLE_USER);
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setRegistrationDate(localDateTime);
        userModel.setLastUpdate(localDateTime);
        mailService.sendMailMessage(new MailDto(
                "gcaio7463@gmail.com",
                userModel.getEmail(),
                "Welcome to our website",
                "We hope that you have a wonderful experience :)"
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userModel));
    }

    @DeleteMapping("/delete/{uuid}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "uuid") UUID uuid) {
        var optionalUserModel = userService.findUserById(uuid);
        if (optionalUserModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found :(");
        }
        userService.deleteUserById(uuid);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping("/edit/{uuid}")
    public ResponseEntity<Object> editUser(@PathVariable(value = "uuid") UUID uuid,
                                           @RequestBody @Valid UserDto userDto) throws JsonProcessingException {
        var optionalUserModel = userService.findUserById(uuid);
        if (optionalUserModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found :(");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUuid(optionalUserModel.get().getUuid());
        userModel.setRole(optionalUserModel.get().getRole());
        userModel.setStatus(optionalUserModel.get().getStatus());
        userModel.setRegistrationDate(optionalUserModel.get().getRegistrationDate());
        userModel.setLastUpdate(localDateTime);
        userService.sendUserMessage(new UserMailDto(
                userModel.getUuid(),
                userModel.getEmail(),
                userModel.getName()
        ));
        return ResponseEntity.ok(userService.saveUser(userModel));
    }

    @GetMapping("/all")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Object> listAllUsers(@PageableDefault Pageable pageable) {
        var allUsers = userService.findAllUsers(pageable);
        if (allUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found :(");
        }
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/one/{uuid}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Object> listUserById(@PathVariable(value = "uuid") UUID uuid) {
        var optionalUserModel = userService.findUserById(uuid);
        return optionalUserModel.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found :("));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody UserLoginDto userLoginDto) {
        var authentication = userService.authenticateUser(userLoginDto);
        if(authentication.equals("EmailNotFound")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found :(");
        }
        if(authentication.equals("IncorrectPassword")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Password!");
        }
        return ResponseEntity.ok(authentication);
    }

    @GetMapping("/validate-token")
    @ResponseBody
    public AuthModel validateToken(@RequestParam(value="token") @NotNull String token) {
        try {
            if(jwtService.validateToken(token)) {
                var optionalUserModel = userService.findUserByEmail(jwtService.getEmailByToken(token));
                if (optionalUserModel.isPresent()) {
                    return new AuthModel(
                            optionalUserModel.get().getEmail(),
                            optionalUserModel.get().getPassword(),
                            optionalUserModel.get().getRole().name());
                }
            }
            return null;
        } catch(Exception exception) {
            return null;
        }
    }

}
