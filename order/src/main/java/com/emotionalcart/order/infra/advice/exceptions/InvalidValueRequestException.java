package com.emotionalcart.order.infra.advice.exceptions;

public class InvalidValueRequestException extends RuntimeException {

    public InvalidValueRequestException(String message) {
        super(message);
    }

}
