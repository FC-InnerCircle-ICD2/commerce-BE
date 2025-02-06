package com.emotionalcart.order.infra.advice.exceptions;

/**
 * 상품 가격 조회 실패시 예외처리
 */
public class ProductPriceException extends RuntimeException {

    public ProductPriceException(String message) {
        super(message);
    }

}

