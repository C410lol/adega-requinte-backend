package com.adega.ms.mail.adegamailsservice.listeners;

import com.adega.ms.mail.adegamailsservice.dtos.MailDto;
import com.adega.ms.mail.adegamailsservice.services.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailListener {

    private final MailService mailService;
    private final ObjectMapper objectMapper;

    private MailDto convertJsonToEmailDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, MailDto.class);
    }

    @KafkaListener(groupId = "mail_service", topics = {"mails"})
    public void listenMailMessages(String emailDtoJson) {
        try {
            mailService.saveAndSend(convertJsonToEmailDto(emailDtoJson));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
