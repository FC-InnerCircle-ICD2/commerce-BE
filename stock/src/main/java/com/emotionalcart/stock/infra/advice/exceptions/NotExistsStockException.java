package com.emotionalcart.stock.infra.advice.exceptions;

public class NotExistsStockException extends RuntimeException {

    public NotExistsStockException() {
        super("해당 상품은 재고가 존재하지 않습니다.");
    }

    public NotExistsStockException(String message) {
        super(message);
    }

}
