package com.emotionalcart.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    /**
     * @Valid 유효성 검증 실패 (DTO 유효성 검사 실패)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation Exception 발생: {}", ex.getMessage(), ex);

        // 첫 번째 오류 메시지만 응답으로 반환
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .orElse("잘못된 요청입니다.");

        ExceptionResponse response = new ExceptionResponse(
            ErrorCode.BAD_REQUEST.getErrorCode(),
            errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException 발생: {}", ex.getMessage(), ex);

        ex.getRequiredType();
        String errorMessage = String.format("%s 값이 올바르지 않습니다. (%s 타입이어야 합니다.)",
                                            ex.getName(),
                                            ex.getRequiredType().getSimpleName());

        ExceptionResponse response = new ExceptionResponse(
            ErrorCode.BAD_REQUEST.getErrorCode(),
            errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public record ExceptionResponse(String errorCode, String errorMessage) {

    }

}