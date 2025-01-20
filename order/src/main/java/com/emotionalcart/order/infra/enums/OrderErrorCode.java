package com.emotionalcart.order.infra.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 주문 에러 코드
 */
@Getter
public enum OrderErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, OrderHttpStatus.BAD_REQUEST, "잘못된 요청입니다. 입력값을 확인해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, OrderHttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

    NULL_VALUE_ERROR(HttpStatus.BAD_REQUEST, OrderHttpStatus.NULL_VALUE_ERROR, "NULL 값이 포함되어 있습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.BAD_REQUEST, OrderHttpStatus.RESOURCE_NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
    INVALID_ORDER(HttpStatus.BAD_REQUEST, OrderHttpStatus.INVALID_ORDER, "유효하지 않은 주문입니다.");

    private final HttpStatus status;
    private final OrderHttpStatus code;
    private final String message;

    OrderErrorCode(HttpStatus status, OrderHttpStatus code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getErrorCode() {
        return this.code.getCode();
    }

}
