package com.emotionalcart.shipment.domain.enums;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 배송 상태(ShipmentStatus)를 정의하는 열거형(enum) 클래스.
 * 각 상태는 설명(description)을 포함하며, 유효한 상태 전환(validTransitions)을 관리함.
 */
@Getter
public enum ShipmentStatus {
    SHIP_REQUESTED("배송 요청됨"),       // 운송장 번호 없음, 출고 대기
    PENDING("배송 준비중"), // 업체가 해당 배송을 확인 후 처리 상태로 변경
    SHIPPED("출고 완료"),
    DELIVERING("배송 중"),      // 운송장 번호 부여됨
    OUT_FOR_DELIVERY("배송 준비 완료"), // 최종 배송 준비 완료
    DELIVERED("배송 완료"),     // 배송 완료됨
    CANCELLED("배송 취소");    // 배송 취소됨

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    private static final Map<ShipmentStatus, Set<ShipmentStatus>> validTransitions = new EnumMap<>(ShipmentStatus.class);

    static {
        validTransitions.put(SHIP_REQUESTED, Set.of(PENDING, CANCELLED));
        validTransitions.put(PENDING, Set.of(SHIPPED, DELIVERING, CANCELLED));
        validTransitions.put(SHIPPED, Set.of(DELIVERING));
        validTransitions.put(DELIVERING, Set.of(OUT_FOR_DELIVERY, DELIVERED));
        validTransitions.put(OUT_FOR_DELIVERY, Set.of(DELIVERED));
        validTransitions.put(DELIVERED, Set.of());
        validTransitions.put(CANCELLED, Set.of());
    }

    /**
     * 현재 상태에서 주어진 상태(newStatus)로 변경할 수 있는지 확인.
     *
     * @param newStatus 변경하려는 배송 상태
     * @return 상태 전환 가능 여부 (true: 가능, false: 불가능)
     */
    public boolean canTransitionTo(ShipmentStatus newStatus) {
        return validTransitions.getOrDefault(this, Set.of()).contains(newStatus);
    }
}