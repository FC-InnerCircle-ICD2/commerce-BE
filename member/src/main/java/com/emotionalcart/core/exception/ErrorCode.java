package com.emotionalcart.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 시스템 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER-0001", "알 수 없는 오류가 발생했습니다."),


    // 인증 && 인가


    // 유저

    // 백오피스
    INVALID_MEMBER_ROLE_VALUE(HttpStatus.BAD_REQUEST, "MEMBER-1000", "memberRole 값에 부적절한 입력 값이 포함되어 있습니다."),
    INVALID_ADMIN_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-1000", "존재하지 않는 관리자 계정 입니다.");



    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
