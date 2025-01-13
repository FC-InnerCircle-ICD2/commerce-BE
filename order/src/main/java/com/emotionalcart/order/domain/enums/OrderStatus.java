package com.emotionalcart.order.domain.enums;

public enum OrderStatus {
    PENDING,            // 주문 대기
    PAYMENT_REQUESTED,  // 결제 요청
    PAYMENT_COMPLETED,  // 결제 완료
    PAYMENT_FAILED,     // 결제 실패
    COMPLETED,          // 주문 완료
    CANCELLED,          // 주문 취소
    SHIP_REQUESTED,     // 배송 요청
    READY_TO_SHIP,      // 배송 준비
    SHIPPING,           // 배송 중
    DELIVERED,          // 배송 완료
    REFUNDED            // 환불
}
