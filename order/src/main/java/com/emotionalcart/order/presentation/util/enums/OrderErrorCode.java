package com.emotionalcart.order.presentation.util.enums;

import lombok.Getter;

/**
 * 주문 에러 코드
 */
@Getter
public enum OrderErrorCode {

    BAD_REQUEST(CommonHttpStatus.BAD_REQUEST, "잘못된 요청입니다. 입력값을 확인해주세요."),
    INTERNAL_SERVER_ERROR(CommonHttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

    NULL_VALUE_ERROR(OrderHttpStatus.NULL_VALUE_ERROR, "NULL 값이 포함되어 있습니다."),
    RESOURCE_NOT_FOUND(OrderHttpStatus.RESOURCE_NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),

    PRODUCT_STOCK_QUERY_FAIL(ProductHttpStatus.PRODUCT_STOCK_QUERY_FAIL, "상품 재고 조회를 실패하였습니다."),
    PRODUCT_STOCK_UPDATE_FAIL(ProductHttpStatus.PRODUCT_STOCK_UPDATE_FAIL, "상품 재고 변경을 실패하였습니다.");

    private final Enum<?> status;
    private final String message;

    OrderErrorCode(Enum<?> status, String message) {
        this.status = status;
        this.message = message;
    }

}
