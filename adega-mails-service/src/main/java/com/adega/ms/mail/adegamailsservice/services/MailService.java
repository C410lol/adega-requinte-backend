package com.adega.ms.mail.adegamailsservice.services;

import com.adega.ms.mail.adegamailsservice.dtos.MailDto;
import com.adega.ms.mail.adegamailsservice.enums.EmailStatus;
import com.adega.ms.mail.adegamailsservice.models.MailModel;
import com.adega.ms.mail.adegamailsservice.repositories.MailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;

    public void saveAndSend(MailDto mailDto) {
        var mailModel = new MailModel();
        BeanUtils.copyProperties(mailDto, mailModel);
        mailModel.setSendDate(LocalDateTime.now(ZoneId.of("UTC")));
        try {
            var mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailDto.getFrom());
            mailMessage.setTo(mailDto.getTo());
            mailMessage.setSubject(mailDto.getSubject());
            mailMessage.setText(mailDto.getText());
            mailSender.send(mailMessage);
            mailModel.setStatus(EmailStatus.SENT);
            log.info("Email has been sent -> {}", mailDto);
        } catch (MailException e) {
            mailModel.setStatus(EmailStatus.ERROR);
            log.error("{}, mail -> {}", e.getMessage(), mailDto);
        } finally {
            mailRepository.save(mailModel);
            log.info("Email has been saved -> {}", mailModel);
        }
    }

}
