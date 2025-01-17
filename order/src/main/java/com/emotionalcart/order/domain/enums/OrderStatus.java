package com.emotionalcart.order.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    /**
     * 주문 대기
     */
    PENDING("주문 대기"),               // 주문 대기
    /**
     * 결제 요청
     */
    PAYMENT_REQUESTED("결제 요청"),
    /**
     * 결제 완료
     */
    PAYMENT_COMPLETED("결제 완료"),
    /**
     * 결제 실패
     */
    PAYMENT_FAILED("결제 실패"),
    /**
     * 주문 완료
     */
    COMPLETED("주문 완료"),
    /**
     * 주문 취소
     */
    CANCELLED("주문 취소"),
    /**
     * 배송 요청
     */
    SHIP_REQUESTED("배송 요청"),
    /**
     * 배송 준비
     */
    READY_TO_SHIP("배송 준비"),
    /**
     * 배송 중
     */
    SHIPPING("배송중"),
    /**
     * 배송 완료
     */
    DELIVERED("배송 완료"),
    /**
     * 환불
     */
    REFUNDED("환불");
    private final String statusName;

}
