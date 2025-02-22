package com.emotionalcart.order.infra.advice.exceptions;

public class StockException extends RuntimeException {

    public StockException(String message) {
        super(message);
    }

}
