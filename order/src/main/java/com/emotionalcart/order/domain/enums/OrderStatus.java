package com.emotionalcart.order.domain.enums;

public enum OrderStatus {
    PENDING,            // 주문 대기
    PAYMENT_PENDING,    // 결제 대기
    PAYMENT_COMPLETED,  // 결제 완료
    PAYMENT_FAILED,     // 결제 실패
    COMPLETED,          // 주문 완료
    CANCELLED,          // 주문 취소
    READY_TO_SHIP,      // 배송 준비
    SHIPPING,           // 배송 중
    REFUNDED            // 환불
}
