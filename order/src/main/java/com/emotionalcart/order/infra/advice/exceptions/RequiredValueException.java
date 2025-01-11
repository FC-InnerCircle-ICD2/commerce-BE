package com.emotionalcart.order.infra.advice.exceptions;

public class RequiredValueException extends RuntimeException {

    public RequiredValueException(String message) {
        super(message);
    }

}
