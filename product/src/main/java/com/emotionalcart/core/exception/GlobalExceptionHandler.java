package com.emotionalcart.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

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

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ExceptionResponse> handleHandlerMethodValidationException(Exception ex) {
        log.error("Exception 발생: {}", ex.getMessage(), ex);

        ExceptionResponse response = new ExceptionResponse(
                ErrorCode.BAD_REQUEST.getErrorCode(),
                ErrorCode.BAD_REQUEST.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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


    public record ExceptionResponse(String errorCode, String errorMessage) {
    }
}