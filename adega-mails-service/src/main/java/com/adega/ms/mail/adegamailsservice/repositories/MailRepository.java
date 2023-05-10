package com.adega.ms.mail.adegamailsservice.repositories;

import com.adega.ms.mail.adegamailsservice.models.MailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MailRepository extends JpaRepository<MailModel, UUID> {
}
