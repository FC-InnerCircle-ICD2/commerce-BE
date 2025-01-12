package com.emotionalcart.order.presentation.util.enums;

import lombok.Getter;

/**
 * 주문 상태코드
 */
@Getter
public enum OrderHttpStatus {

    NULL_VALUE_ERROR("ORDER-101"),
    RESOURCE_NOT_FOUND("ORDER-102");

    private final String code;

    OrderHttpStatus(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }
}
