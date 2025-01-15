package com.emotionalcart.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    public ProductException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getMessage();
    }
}