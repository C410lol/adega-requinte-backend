package com.adega.ms.mail.adegamailsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MailDto {

    private String from;
    private String to;
    private String subject;
    private String text;

}
