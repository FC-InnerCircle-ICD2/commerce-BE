package com.emotionalcart.product.presentation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(3000, "알 수 없는 오류가 발생했습니다." ,HttpStatus.INTERNAL_SERVER_ERROR);

    private final int errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int errorCode, String message, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}