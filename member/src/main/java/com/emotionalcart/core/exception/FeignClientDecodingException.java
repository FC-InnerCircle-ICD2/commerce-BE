package com.emotionalcart.core.exception;

public class FeignClientDecodingException extends RuntimeException {

    public FeignClientDecodingException(String message) {
        super(message);
    }

}
