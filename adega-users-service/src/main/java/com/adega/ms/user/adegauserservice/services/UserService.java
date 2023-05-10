package com.adega.ms.user.adegauserservice.services;

import com.adega.ms.user.adegauserservice.dtos.UserLoginDto;
import com.adega.ms.user.adegauserservice.dtos.UserMailDto;
import com.adega.ms.user.adegauserservice.models.UserModel;
import com.adega.ms.user.adegauserservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService {

    private final JwtService jwtService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String authenticateUser(@NotNull UserLoginDto userLoginDto) {
        Optional<UserModel> optionalUserModel = findUserByEmail(userLoginDto.getEmail());
        if (optionalUserModel.isPresent()) {
            if (passwordEncoder.matches(userLoginDto.getPassword(),
                    optionalUserModel.get().getPassword())) {
                log.info("User {} has been authenticated", userLoginDto);
                return jwtService.generateToken(optionalUserModel.get().getEmail());
            }
            return "IncorrectPassword";
        }
        return "EmailNotFound";
    }

    private String convertUserMailDtoToJson(UserMailDto userMailDto) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userMailDto);
    }

    public boolean cpfAlreadyExists(String cpf) {
        return userRepository.existsByUserCpf(cpf);
    }

    public void deleteUserById(UUID uuid) {
        userRepository.deleteById(uuid);
        log.info("User has been deleted, Id -> {}", uuid);
        sendUserDeleteMessage(uuid);
    }

    public boolean emailAlreadyExists(String email) {
        return userRepository.existsByUserEmail(email);
    }

    public Page<UserModel> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<UserModel> findUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public Optional<UserModel> findUserById(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public UserModel saveUser(@NotNull UserModel userModel) throws JsonProcessingException {
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(userModel);
        log.info("User has been saved -> {}", userModel);
        sendUserMessage(new UserMailDto(
                userModel.getUuid(),
                userModel.getEmail(),
                userModel.getName()
        ));
        return userModel;
    }

    public void sendUserDeleteMessage(UUID id) {
        kafkaTemplate.send("users_delete", String.valueOf(id));
    }

    public void sendUserMessage(UserMailDto userMailDto) throws JsonProcessingException {
        kafkaTemplate.send("users", convertUserMailDtoToJson(userMailDto));
    }

}
