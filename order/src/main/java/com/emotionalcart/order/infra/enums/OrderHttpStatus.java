package com.emotionalcart.order.infra.enums;

import lombok.Getter;

/**
 * 주문 상태코드
 */
@Getter
public enum OrderHttpStatus {

    NULL_VALUE_ERROR("ORDER-0001"),
    RESOURCE_NOT_FOUND("ORDER-0002"),
    BAD_REQUEST("ORDER-0003"),
    INTERNAL_SERVER_ERROR("ORDER-0004"),
    INVALID_ORDER("ORDER-0005"),
    PRICE_NOT_FOUND("ORDER-0006"),
    STOCK_NOT_FOUND("ORDER-0007"),
    INVALID_PRODUCT("ORDER-0008"),
    ;

    private final String code;

    OrderHttpStatus(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }
}
