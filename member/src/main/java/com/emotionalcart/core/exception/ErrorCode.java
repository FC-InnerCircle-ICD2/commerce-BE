package com.emotionalcart.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 시스템 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER-0001", "알 수 없는 오류가 발생했습니다."),


    // 인증 && 인가


    // 유저
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER-0002", "해당 회원을 찾을 수 없습니다."),
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
