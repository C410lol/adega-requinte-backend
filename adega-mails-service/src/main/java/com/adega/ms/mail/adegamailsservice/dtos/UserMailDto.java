package com.adega.ms.mail.adegamailsservice.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Table(name = "user_tb")
@ToString
public class UserMailDto {

    @Id
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserMailDto that = (UserMailDto) o;
        return getUuid() != null && Objects.equals(getUuid(), that.getUuid());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }

}

