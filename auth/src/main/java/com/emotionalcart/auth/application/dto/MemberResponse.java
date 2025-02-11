package com.emotionalcart.auth.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponse {
    private String role;
    private String name;
    private String userName;
}
