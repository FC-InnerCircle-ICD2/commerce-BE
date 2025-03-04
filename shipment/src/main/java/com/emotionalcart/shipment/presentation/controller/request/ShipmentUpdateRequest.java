package com.emotionalcart.shipment.presentation.controller.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShipmentUpdateRequest {

    private ShipmentUpdateStatus status;

    @Getter
    public enum ShipmentUpdateStatus {
        SHIP_REQUESTED("배송 요청됨"),       // 운송장 번호 없음, 출고 대기
        PENDING("배송 준비중"), // 업체가 해당 배송을 확인 후 처리 상태로 변경
        SHIPPED("출고 완료"),
        DELIVERING("배송 중"),      // 운송장 번호 부여됨
        OUT_FOR_DELIVERY("배송 준비 완료"), // 최종 배송 준비 완료
        DELIVERED("배송 완료"),     // 배송 완료됨
        CANCELLED("배송 취소");    // 배송 취소됨

        private final String description;

        ShipmentUpdateStatus(String description) {
            this.description = description;
        }
    }

}
