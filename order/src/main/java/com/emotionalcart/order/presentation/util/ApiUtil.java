package com.emotionalcart.order.presentation.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * API 응답 생성을 위한 유틸리티 클래스
 */
public class ApiUtil {

    /**
     * 성공 응답 생성
     *
     * @param <T>    응답 데이터 타입
     * @param body   응답 본문 데이터
     * @param status HTTP 상태 코드
     * @return ResponseEntity<T> 성공 응답
     */
    public static <T> ResponseEntity<T> createResponse(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    /**
     * 에러 응답 생성
     *
     * @param status       HTTP 상태 코드
     * @param errorMessage 에러 메시지
     * @return ResponseEntity<ErrorResponse> 에러 응답
     */
    public static ResponseEntity<ErrorResponse> error(HttpStatus status, String errorMessage) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorMessage);
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * 에러 응답용 객체
     */
    public static class ErrorResponse {

        private final int errorCode;
        private final String errorMessage;

        public ErrorResponse(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }

}
