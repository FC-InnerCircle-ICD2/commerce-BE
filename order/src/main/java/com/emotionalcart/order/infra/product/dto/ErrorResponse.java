package com.emotionalcart.order.infra.product.dto;

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
