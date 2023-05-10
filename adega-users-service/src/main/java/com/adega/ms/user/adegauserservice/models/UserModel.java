package com.adega.ms.user.adegauserservice.models;

import com.adega.ms.user.adegauserservice.enums.UserRole;
import com.adega.ms.user.adegauserservice.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
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
public class UserModel {

    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String birthdate;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserModel userModel = (UserModel) o;
        return getUuid() != null && Objects.equals(getUuid(), userModel.getUuid());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }

}
