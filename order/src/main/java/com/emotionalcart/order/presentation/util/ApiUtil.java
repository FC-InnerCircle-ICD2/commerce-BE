package com.emotionalcart.order.presentation.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

public class ApiUtil {

    public static <T> ApiResult<T> ok(T data){
        return new ApiResult<>(HttpStatus.OK, data);
    }

    public static <T> ApiResult<T> error(HttpStatus httpStatus, String errorMessage){
        return new ApiResult<>(httpStatus.value(), errorMessage);
    }

    @Data
    public static class ApiResult<T>{
        private HttpStatus status;
        private T data;
        private int errorCode;
        private String errorMessage;

         private ApiResult(HttpStatus status, T data) {
            this.status = status;
            this.data = data;
        }

        private ApiResult(int errorCode, String errorMessage){
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }
}
