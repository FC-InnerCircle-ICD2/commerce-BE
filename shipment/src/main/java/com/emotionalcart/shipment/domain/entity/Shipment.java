package com.emotionalcart.shipment.domain.entity;

import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 운송장 번호 (초기에는 NULL)
     */
    private String trackingNumber;

    /**
     * READY → IN_TRANSIT → DELIVERED
     */
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    /**
     * 배송 요청 시간
     */
    private LocalDateTime requestedAt;

    /**
     * 배송 시작 시간
     */
    private LocalDateTime shippedAt;

    /**
     * 배송 완료 시간
     */
    private LocalDateTime deliveredAt;

    /**
     * 주문번호
     */
    private Long orderId;
    
    // 배송 요청 (운송장 번호 없이 생성)
    public static Shipment requestShipment(Long orderId) {
        Shipment shipment = new Shipment();
        shipment.orderId = orderId;
        shipment.status = ShipmentStatus.READY; // 초기 상태는 READY
        shipment.requestedAt = LocalDateTime.now();
        return shipment;
    }

    // 배송 시작 (운송장 번호 생성)
    public void startShipment(String trackingNumber) {
        this.trackingNumber = trackingNumber;
        this.status = ShipmentStatus.IN_TRANSIT; // 배송 중 상태 변경
        this.shippedAt = LocalDateTime.now();
    }

    // 배송 완료 처리
    public void completeDelivery() {
        this.status = ShipmentStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

}
