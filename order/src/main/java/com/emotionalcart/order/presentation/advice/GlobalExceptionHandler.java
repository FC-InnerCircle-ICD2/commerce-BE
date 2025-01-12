package com.emotionalcart.order.presentation.advice;

import com.emotionalcart.order.presentation.advice.exceptions.ProductStockException;
import com.emotionalcart.order.presentation.util.enums.CommonHttpStatus;
import com.emotionalcart.order.presentation.util.enums.OrderErrorCode;
import com.emotionalcart.order.presentation.util.enums.OrderHttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        log.error("IllegalArgumentException 발생: {}", ex.getMessage());

        ErrorResponse errorResponse =
            new ErrorResponse(CommonHttpStatus.BAD_REQUEST.value(), OrderErrorCode.BAD_REQUEST.getMessage());
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
        log.error("Exception 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse =
            new ErrorResponse(CommonHttpStatus.INTERNAL_SERVER_ERROR.value(), OrderErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    /**
     * 상품 재고 예외처리
     *
     * @param ex 발생한 Exception
     * @return ResponseEntity<ErrorResponse> 서버 오류 응답
     */
    @ExceptionHandler(ProductStockException.class)
    public ResponseEntity<ErrorResponse> productStockException(ProductStockException ex) {
        // 예외 메시지 로깅
        log.error("Exception 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse =
            new ErrorResponse(OrderHttpStatus.RESOURCE_NOT_FOUND.value(), OrderErrorCode.RESOURCE_NOT_FOUND.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    public record ErrorResponse(String errorCode, String errorMessage) {

    }

}
