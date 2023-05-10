package com.adega.ms.user.adegauserservice.repositories;

import com.adega.ms.user.adegauserservice.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    boolean existsByUserCpf(String cpf);
    boolean existsByUserEmail(String email);
    Optional<UserModel> findByUserEmail(String email);

}
