package com.adega.ms.user.adegauserservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String birthdate;

    @CPF(message = "Invalid CPF!")
    @NotBlank
    private String cpf;

    @Email(message = "Invalid email!")
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

}
