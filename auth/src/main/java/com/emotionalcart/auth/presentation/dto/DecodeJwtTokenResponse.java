package com.emotionalcart.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecodeJwtTokenResponse {
    private Long userId;
    private String role;
}
