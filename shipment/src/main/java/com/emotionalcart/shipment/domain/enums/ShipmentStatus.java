package com.emotionalcart.shipment.domain.enums;

import lombok.Getter;

@Getter
public enum ShipmentStatus {
    READY("배송 요청됨"),       // 운송장 번호 없음, 출고 대기
    IN_TRANSIT("배송 중"),      // 운송장 번호 부여됨
    OUT_FOR_DELIVERY("배송 준비 완료"), // 최종 배송 준비 완료
    DELIVERED("배송 완료"),     // 배송 완료됨
    CANCELLED("배송 취소");    // 배송 취소됨

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
