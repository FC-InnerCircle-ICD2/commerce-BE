package com.emotionalcart.order.infra.advice.exceptions;

public class RedissonLockException extends RuntimeException {

    public RedissonLockException(String message) {
        super(message);
    }

}
