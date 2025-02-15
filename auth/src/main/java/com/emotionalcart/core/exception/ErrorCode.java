package com.emotionalcart.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 시스템 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-0001", "알 수 없는 오류가 발생했습니다."),

    // 인증 && 인가
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "AUTH-0002", "인증 정보가 없습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-0003", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-0004", "유효하지 않은 토큰입니다."),

    // 유저
    ;


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
