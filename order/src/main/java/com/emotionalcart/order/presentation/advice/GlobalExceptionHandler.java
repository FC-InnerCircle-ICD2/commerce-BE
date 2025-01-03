package com.emotionalcart.order.presentation.advice;

import com.emotionalcart.order.presentation.util.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiUtil.ApiResult<Void> illegalArgumentException(IllegalArgumentException ex) {
        return ApiUtil.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiUtil.ApiResult<Void> globalException(IllegalArgumentException ex) {
        return ApiUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}
