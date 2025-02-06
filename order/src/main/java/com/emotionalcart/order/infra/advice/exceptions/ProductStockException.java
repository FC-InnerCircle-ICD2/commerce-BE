package com.emotionalcart.order.infra.advice.exceptions;

/**
 * 상품 재고 예외처리
 */
public class ProductStockException extends RuntimeException {

    public ProductStockException(String message) {
        super(message);
    }

}
