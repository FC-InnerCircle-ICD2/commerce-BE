package com.emotionalcart.order.infra.advice.exceptions;

/**
 * 상품 검증 예외처리
 */
public class ProductValidationException extends RuntimeException {

    public ProductValidationException(String message) {
        super(message);
    }

}

