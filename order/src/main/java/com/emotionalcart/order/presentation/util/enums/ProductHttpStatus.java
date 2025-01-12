package com.emotionalcart.order.presentation.util.enums;

/**
 * 상품 관련 상태코드
 */
public enum ProductHttpStatus {

    PRODUCT_STOCK_QUERY_FAIL("ORDER-103"),
    PRODUCT_STOCK_UPDATE_FAIL("ORDER-104");

    private final String code;

    ProductHttpStatus(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }
}
