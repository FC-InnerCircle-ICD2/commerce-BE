package com.emotionalcart.member.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAdminMember {

    private String email;

    private String password;

    private String phone;

    private String userName;

}
