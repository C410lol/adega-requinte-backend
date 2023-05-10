package com.adega.ms.user.adegauserservice.dtos;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MailDto {

    private String from;
    private String to;
    private String subject;
    private String text;

}
