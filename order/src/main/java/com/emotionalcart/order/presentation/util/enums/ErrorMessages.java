package com.emotionalcart.order.presentation.util.enums;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    BAD_REQUEST("잘못된 요청입니다. 입력값을 확인해주세요."),
    INTERNAL_SERVER_ERROR("서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    NULL_VALUE_ERROR("NULL 값이 포함되어 있습니다."),
    RESOURCE_NOT_FOUND("요청하신 리소스를 찾을 수 없습니다.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}
