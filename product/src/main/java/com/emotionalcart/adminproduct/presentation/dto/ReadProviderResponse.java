package com.emotionalcart.adminproduct.presentation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadProviderResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long providerId;
    private String name;
    private String description;
    private Long memberId;

}
