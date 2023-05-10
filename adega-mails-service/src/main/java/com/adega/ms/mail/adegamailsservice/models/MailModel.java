package com.adega.ms.mail.adegamailsservice.models;

import com.adega.ms.mail.adegamailsservice.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Table(name = "mail_tb")
@ToString
public class MailModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private String from;

    @Column(nullable = false)
    private String to;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EmailStatus status;

    @Column(nullable = false)
    private LocalDateTime sendDate;

}
