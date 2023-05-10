package com.adega.ms.mail.adegamailsservice.listeners;

import com.adega.ms.mail.adegamailsservice.dtos.UserMailDto;
import com.adega.ms.mail.adegamailsservice.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserListener {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    private UserMailDto convertJsonToUserMailDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, UserMailDto.class);
    }

    @KafkaListener(groupId = "mail_consumers", topics = {"mails_products"})
    public void listenProductMailMessage(String productName) {
        userService.sendProductMailToAll(productName);
    }

    @KafkaListener(groupId = "mail_service", topics = {"users_delete"})
    public void listenUserDeleteMessages(String userId) {
        userService.deleteById(UUID.fromString(userId));
    }

    @KafkaListener(groupId = "mail_service", topics = {"users"})
    public void listenUserMessages(String userMailDtoJson) {
        try {
            userService.save(convertJsonToUserMailDto(userMailDtoJson));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
