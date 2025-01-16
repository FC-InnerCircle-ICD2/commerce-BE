package com.emotionalcart.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("PRODUCT-0001", "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    , INVALID_REQUEST("PRODUCT-0005","잘못된 요청입니다.",HttpStatus.BAD_REQUEST)
    , UNAUTHORIZE_ERROR("PRODUCT-0002","인증에 실패했습니다.",HttpStatus.UNAUTHORIZED)
//    , FORBIDDEN(403, "허용되지 않은 접근입니다.")
    , DATA_NOT_FOUND("PRODUCT-0003","데이터를 찾을 수 없습니다.",HttpStatus.NOT_FOUND)
    , NOT_FOUND_PRODUCT("PRODUCT-0004","상품을 찾을 수 없습니다.",HttpStatus.NOT_FOUND)
//    , BASEURL_BAD_REQUEST(600, "기본 주소 오류입니다.")
//    , BAD_PARSING(700,"JSON 파일 파싱 에러입니다.")
    ;
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String errorCode, String message, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
