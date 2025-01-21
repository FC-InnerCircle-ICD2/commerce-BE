package com.emotionalcart.order.infra.enums;

import lombok.Getter;

/**
 * 주문 에러 코드
 */
@Getter
public enum OrderErrorCode {

    BAD_REQUEST(OrderHttpStatus.BAD_REQUEST, "잘못된 요청입니다. 입력값을 확인해주세요."),
    INTERNAL_SERVER_ERROR(OrderHttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

    NULL_VALUE_ERROR(OrderHttpStatus.NULL_VALUE_ERROR, "NULL 값이 포함되어 있습니다."),
    RESOURCE_NOT_FOUND(OrderHttpStatus.RESOURCE_NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
    INVALID_ORDER(OrderHttpStatus.INVALID_ORDER, "유효하지 않은 주문입니다."),
    PRICE_NOT_FOUND(OrderHttpStatus.PRICE_NOT_FOUND, "상품 가격 정보를 조회하는 중 오류가 발생했습니다."),
    STOCK_NOT_FOUND(OrderHttpStatus.STOCK_NOT_FOUND, "상품 재고 변경하는 과정에서 오류가 발생했습니다."),
    INVALID_PRODUCT(OrderHttpStatus.INVALID_PRODUCT, "상품 조회 과정에서 오류가 발생했습니다.");

    private final OrderHttpStatus code;
    private final String message;

    OrderErrorCode(OrderHttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getErrorCode() {
        return this.code.getCode();
    }

}
