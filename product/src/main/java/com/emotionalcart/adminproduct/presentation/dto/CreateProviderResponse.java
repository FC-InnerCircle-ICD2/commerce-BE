package com.emotionalcart.adminproduct.presentation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProviderResponse {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long providerId;
}
