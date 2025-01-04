package com.emotionalcart.order.presentation.advice;

import com.emotionalcart.order.presentation.util.ApiUtil;
import com.emotionalcart.order.presentation.util.enums.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
     * @return ResponseEntity<ApiUtil.ErrorResponse> 잘못된 요청 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiUtil.ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 예외 메시지 로깅
        log.error("IllegalArgumentException 발생: {}", ex.getMessage());

        // 사용자 친화적인 메시지 반환
        return ApiUtil.error(HttpStatus.BAD_REQUEST, ErrorMessages.BAD_REQUEST.getMessage());
    }

    /**
     * 모든 예외 처리
     *
     * @param ex 발생한 Exception
     * @return ResponseEntity<ApiUtil.ErrorResponse> 서버 오류 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiUtil.ErrorResponse> handleGlobalException(Exception ex) {
        // 예외 메시지 로깅
        log.error("Exception 발생: {}", ex.getMessage(), ex);

        // 사용자 친화적인 메시지 반환
        return ApiUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
    }

}
