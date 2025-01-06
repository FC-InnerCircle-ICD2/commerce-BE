package com.emotionalcart.order.presentation.util.enums;

import lombok.Getter;

/**
 * 주문 상태코드
 */
@Getter
public enum OrderHttpStatus {

    NULL_VALUE_ERROR(5000),
    RESOURCE_NOT_FOUND(5001);

    private final int code;

    OrderHttpStatus(int code) {
        this.code = code;
    }

    public int value() {
        return code;
    }
}
