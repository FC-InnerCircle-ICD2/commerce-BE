package com.emotionalcart.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 시스템 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER-0001", "알 수 없는 오류가 발생했습니다."),


    // 인증 && 인가
    MEMBER_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "MEMBER-0003", "인증 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "MEMBER-0004", "인증 정보가 없습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "MEMBER-0005", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "MEMBER-0006", "유효하지 않은 토큰입니다."),

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
