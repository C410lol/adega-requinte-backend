package com.adega.ms.user.adegauserservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserMailDto {

    private UUID uuid;
    private String email;
    private String name;

}
