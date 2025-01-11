package com.emotionalcart.order.presentation.controller.request;

/**
 * 결제수단
 */
public enum PaymentMethod {
    BANK_TRANSFER, // 무통장입금
    CARD,
    KAKAO_PAY,
    TOSS,
    NAVER_PAY
}
