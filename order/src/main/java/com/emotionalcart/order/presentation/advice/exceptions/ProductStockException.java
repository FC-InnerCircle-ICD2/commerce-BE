package com.emotionalcart.order.presentation.advice.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * 상품 재고 예외처리
 */
@Slf4j
public class ProductStockException extends RuntimeException {

    public ProductStockException(Object body) {
        log.error("ProductStockException :: {}", body.toString());
    }

}
