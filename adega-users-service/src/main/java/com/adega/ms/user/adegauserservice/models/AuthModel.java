package com.adega.ms.user.adegauserservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AuthModel {

    private String principal;
    private String credentials;
    private String authorities;

}
