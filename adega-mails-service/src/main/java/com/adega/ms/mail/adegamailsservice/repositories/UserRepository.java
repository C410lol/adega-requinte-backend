package com.adega.ms.mail.adegamailsservice.repositories;

import com.adega.ms.mail.adegamailsservice.dtos.UserMailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserMailDto, UUID> {
}
