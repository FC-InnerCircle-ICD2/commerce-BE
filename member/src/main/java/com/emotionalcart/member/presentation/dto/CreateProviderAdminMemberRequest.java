package com.emotionalcart.member.presentation.dto;

import com.emotionalcart.member.application.CreateAdminMember;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class CreateProviderAdminMemberRequest {

    private Long providerId;

    @Email
    private String email;

    private String password;

    private String phone;

    private String userName;

    public CreateAdminMember mapToCommand() {
        return CreateAdminMember.builder()
            .email(email)
            .password(password)
            .phone(phone)
            .userName(userName)
            .build();
    }

}
