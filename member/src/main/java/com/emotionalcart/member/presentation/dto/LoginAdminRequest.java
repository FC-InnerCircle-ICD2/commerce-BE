package com.emotionalcart.member.presentation.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class LoginAdminRequest {

    @Email
    private String email;

    private String password;

}
