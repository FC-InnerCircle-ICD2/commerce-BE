package com.emotionalcart.order.infra.advice.exceptions;

public class FeignClientDecodingException extends RuntimeException {

    public FeignClientDecodingException(String message) {
        super(message);
    }

}
