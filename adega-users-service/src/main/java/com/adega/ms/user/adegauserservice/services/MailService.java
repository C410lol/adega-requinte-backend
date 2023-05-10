package com.adega.ms.user.adegauserservice.services;

import com.adega.ms.user.adegauserservice.dtos.MailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMailMessage(MailDto mailDto) throws JsonProcessingException {
        kafkaTemplate.send("mails", convertEmailDtoToJson(mailDto));
    }

    private String convertEmailDtoToJson(MailDto mailDto) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(mailDto);
    }

}
