package com.emotionalcart.member.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadMemberResponse {
    private Long userId;
    private String role;
    private String name;
}
