package com.emotionalcart.order.infra.product.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ErrorResponse {

    private String errorCode;

    private String errorMessage;

}
