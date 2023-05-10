package com.adega.ms.mail.adegamailsservice.services;

import com.adega.ms.mail.adegamailsservice.dtos.MailDto;
import com.adega.ms.mail.adegamailsservice.dtos.UserMailDto;
import com.adega.ms.mail.adegamailsservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final MailService mailService;
    private final UserRepository userRepository;

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public void save(@NotNull UserMailDto userMailDto) {
        var user = userRepository.findById(userMailDto.getUuid());
        if (user.isPresent()) {
            user.get().setUuid(userMailDto.getUuid());
            user.get().setEmail(userMailDto.getEmail());
            user.get().setName(userMailDto.getName());
            userRepository.save(user.get());
            return;
        }
        userRepository.save(userMailDto);
    }

    public void sendProductMailToAll(String productName) {
        userRepository.findAll().forEach(user -> mailService.saveAndSend(new MailDto(
                "gcaio7463@gmail.com",
                user.getEmail(),
                "Hey! New product in the area",
                String.format("Hello %s, come to our website to confer the new product %s! " +
                        "We hope you like it ;)", user.getName(), productName)
        )));
    }

}
