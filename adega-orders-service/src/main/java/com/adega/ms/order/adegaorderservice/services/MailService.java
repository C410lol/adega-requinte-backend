package com.adega.ms.order.adegaorderservice.services;

import com.adega.ms.order.adegaorderservice.dtos.MailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private String convertEmailDtoToJson(MailDto mailDto) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(mailDto);
    }

    public void sendMailMessage(MailDto mailDto) throws JsonProcessingException {
        kafkaTemplate.send("mails", convertEmailDtoToJson(mailDto));
    }

}
