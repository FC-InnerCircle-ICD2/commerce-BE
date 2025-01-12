package com.emotionalcart.order.presentation.util.enums;

/**
 * 공통 에러 코드
 */
public enum CommonHttpStatus {

    BAD_REQUEST("ORDER-004"),
    INTERNAL_SERVER_ERROR("ORDER-005");

    private final String code;

    CommonHttpStatus(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }
}
