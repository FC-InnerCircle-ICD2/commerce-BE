package com.emotionalcart.order.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결제수단
 */
@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    BANK_TRANSFER("무통장입금"), // 무통장입금
    CARD("카드"), // 카드
    KAKAO_PAY("카카오페이"), // 카카오페이
    TOSS("토스"), // 토스
    NAVER_PAY("네이버페이"), // 네이버페이
    ;
    private final String methodName;
}
