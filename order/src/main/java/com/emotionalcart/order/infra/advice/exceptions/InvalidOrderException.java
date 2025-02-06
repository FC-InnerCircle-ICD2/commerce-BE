package com.emotionalcart.order.infra.advice.exceptions;

/**
 * 유효하지 않은 주문에 대한 에러처리
 */
public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException(String message) {
        super(message);
    }

}