package com.emotionalcart.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("PRODUCT-0001", "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZE_ERROR("PRODUCT-0002", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    DATA_NOT_FOUND("PRODUCT-0003", "데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BAD_REQUEST("PRODUCT-0004", "요청에 필요한 값이 비어 있거나 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    INVALID_REQUEST("PRODUCT-0005", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_PRODUCT("PRODUCT-0006", "해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_PRODUCT_OPTION("PRODUCT-0007", "해당 상품 옵션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    OUT_OF_STOCK("PRODUCT-0008", "상품의 재고가 부족합니다.", HttpStatus.CONFLICT),
    REQUIRED_OPTION_MISSING("PRODUCT-0009", "상품의 필수 옵션이 선택되지 않았습니다.", HttpStatus.CONFLICT),

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