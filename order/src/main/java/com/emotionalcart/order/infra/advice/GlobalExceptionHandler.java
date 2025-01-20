package com.emotionalcart.order.infra.advice;

import com.emotionalcart.order.infra.advice.exceptions.InvalidOrderException;
import com.emotionalcart.order.infra.advice.exceptions.InvalidValueRequestException;
import com.emotionalcart.order.infra.advice.exceptions.RequiredValueException;
import com.emotionalcart.order.infra.enums.OrderErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리기
 * <p>
 * 컨트롤러에서 발생하는 예외를 처리하고 일관된 응답을 반환합니다.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalArgumentException 처리
     *
     * @param ex 발생한 IllegalArgumentException
     * @return ResponseEntity<ErrorResponse> 잘못된 요청 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 예외 메시지 로깅
        log.error("IllegalArgumentException 발생: {}", ex.getMessage());

        ErrorResponse errorResponse =
            new ErrorResponse(OrderErrorCode.BAD_REQUEST.getErrorCode(),
                              OrderErrorCode.BAD_REQUEST.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 모든 예외 처리
     *
     * @param ex 발생한 Exception
     * @return ResponseEntity<ErrorResponse> 서버 오류 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        // 예외 메시지 로깅
        log.error("Exception 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse =
            new ErrorResponse(OrderErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
                              OrderErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler({RequiredValueException.class, InvalidValueRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleRequiredValueException(Exception ex) {
        // 예외 메시지 로깅
        log.error("{} 발생: {}", ex.getClass().getName(), ex.getMessage());

        ErrorResponse errorResponse =
            new ErrorResponse(OrderErrorCode.BAD_REQUEST.getErrorCode(),
                              OrderErrorCode.BAD_REQUEST.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 유효하지 않은 주문에 대한 글로벌 에러 처리
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidOrderException(Exception ex) {
        // 예외 메시지 로깅
        log.error("{} 발생: {}", ex.getClass().getName(), ex.getMessage());

        ErrorResponse errorResponse =
            new ErrorResponse(OrderErrorCode.INVALID_ORDER.getErrorCode(),
                              OrderErrorCode.INVALID_ORDER.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    public record ErrorResponse(String errorCode, String errorMessage) {

    }

}
