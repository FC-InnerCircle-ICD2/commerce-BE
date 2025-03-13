package com.emotionalcart.member.infrasturcture.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private String errorCode;

    private String errorMessage;

}
