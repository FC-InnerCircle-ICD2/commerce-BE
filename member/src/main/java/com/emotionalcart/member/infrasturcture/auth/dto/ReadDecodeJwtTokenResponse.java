package com.emotionalcart.member.infrasturcture.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadDecodeJwtTokenResponse {
    private Long userId;
    private String role;
}
