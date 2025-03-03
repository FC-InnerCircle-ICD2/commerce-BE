package com.emotionalcart.stock.infra.advice;

import com.emotionalcart.stock.infra.advice.exceptions.NotExistsStockException;
import com.emotionalcart.stock.infra.advice.exceptions.OutOfStockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.of("STOCK-003", e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException e) {
            log.error("Validation Exception 발생: {}", ex.getMessage());
            message = e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        } else if (ex instanceof HandlerMethodValidationException e) {
            log.error("HandlerMethodValidation Exception 발생: {}", ex.getMessage());
            message = e.getAllErrors().getFirst().getDefaultMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of("STOCK-004", message));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({OutOfStockException.class, NotExistsStockException.class})
    public ResponseEntity<ExceptionResponse> handleNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.of("STOCK-001", e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequest(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.of("STOCK-002", e.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
    public ResponseEntity<ExceptionResponse> handleInternalServerError(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.of("STOCK-002", e.getMessage()));
    }

}
