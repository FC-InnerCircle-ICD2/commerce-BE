package com.emotionalcart.order.infra.advice;

import com.emotionalcart.order.infra.advice.exceptions.*;
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
     * 공통 예외 응답 생성 메서드
     * <p>
     * 예외를 처리하여 로그를 남기고, 클라이언트에 전달할 표준 응답을 생성합니다.
     * </p>
     *
     * @param ex        발생한 예외
     * @param errorCode 응답에 사용할 에러 코드 (OrderErrorCode 참조)
     * @param status    HTTP 상태 코드
     * @return ResponseEntity<ErrorResponse> 표준 에러 응답
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, OrderErrorCode errorCode, HttpStatus status) {
        log.error("{} 발생: {}", ex.getClass().getName(), ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getErrorCode(), errorCode.getMessage());
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * IllegalArgumentException 처리
     * <p>
     * 잘못된 인자가 전달된 경우 예외를 처리합니다.
     * </p>
     *
     * @param ex 발생한 IllegalArgumentException
     * @return ResponseEntity<ErrorResponse> 잘못된 요청 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(ex, OrderErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    /**
     * 모든 예외 처리
     * <p>
     * 지정되지 않은 예외를 처리하며, 서버 오류로 응답합니다.
     * </p>
     *
     * @param ex 발생한 Exception
     * @return ResponseEntity<ErrorResponse> 서버 오류 응답
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        return buildErrorResponse(ex, OrderErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * RequiredValueException 및 InvalidValueRequestException 처리
     * <p>
     * 필수 값 누락 또는 잘못된 값 요청에 대해 처리합니다.
     * </p>
     *
     * @param ex 발생한 예외
     * @return ResponseEntity<ErrorResponse> 잘못된 요청 응답
     */
    @ExceptionHandler({RequiredValueException.class, InvalidValueRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleRequiredValueException(Exception ex) {
        return buildErrorResponse(ex, OrderErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    /**
     * 유효하지 않은 주문에 대한 글로벌 에러 처리
     * <p>
     * InvalidOrderException 발생 시 처리합니다.
     * </p>
     *
     * @param ex 발생한 InvalidOrderException
     * @return ResponseEntity<ErrorResponse> 잘못된 요청 응답
     */
    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidOrderException(Exception ex) {
        return buildErrorResponse(ex, OrderErrorCode.INVALID_ORDER, HttpStatus.BAD_REQUEST);
    }

    /**
     * ProductPriceException 처리
     * <p>
     * 상품 가격 조회에 실패한 경우 처리합니다.
     * </p>
     *
     * @param ex 발생한 ProductPriceException
     * @return ResponseEntity<ErrorResponse> 잘못된 요청 응답
     */
    @ExceptionHandler(ProductPriceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleProductPriceException(Exception ex) {
        return buildErrorResponse(ex, OrderErrorCode.PRICE_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    /**
     * ProductStockException 처리
     * <p>
     * 상품 재고 변경에 실패한 경우 처리
     * </p>
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ProductStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleProductStockException(Exception ex) {
        return buildErrorResponse(ex, OrderErrorCode.STOCK_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    /**
     * ProductValidationException 처리
     * <p>
     * 상품 검증 과정에서 실패한 경우 처리
     * </p>
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ProductValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleProductValidationException(Exception ex) {
        return buildErrorResponse(ex, OrderErrorCode.INVALID_PRODUCT, HttpStatus.BAD_REQUEST);
    }

    /**
     * 에러 응답 모델
     * <p>
     * 클라이언트에게 전달할 에러 정보를 정의합니다.
     * </p>
     *
     * @param errorCode    에러 코드
     * @param errorMessage 에러 메시지
     */
    public record ErrorResponse(String errorCode, String errorMessage) {

    }

}
