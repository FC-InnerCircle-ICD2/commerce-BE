package com.emotionalcart.product.presentation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ExceptionResponse> handleApplicationException(ProductException ex) {
        log.error("Exception 발생: {}", ex.getMessage(), ex);

        ExceptionResponse response = new ExceptionResponse(
                ex.getErrorCode(),
                ex.getErrorMessage()
        );

        return ResponseEntity.status(ex.getHttpStatus())
                             .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        log.error("Exception 발생: {}", ex.getMessage(), ex);

        ExceptionResponse response = new ExceptionResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(response);
    }

    public record ExceptionResponse(int errorCode, String errorMessage) {
    }
}