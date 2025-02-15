package com.emotionalcart.shipment.domain.entity;

import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 업체번호
     */
    private Long providerId;

    /**
     * 주문번호
     */
    private Long orderId;

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
     * 배송 정보
     */
    @Embedded
    private Delivery delivery;

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

    public Shipment(Long providerId,
                    Long orderId,
                    ShipmentStatus shipmentStatus,
                    String name,
                    String phoneNumber,
                    String address,
                    String detailAddress,
                    String zoneCode,
                    String deliveryMemo) {
        this.providerId = providerId;
        this.orderId = orderId;
        this.status = shipmentStatus;
        this.delivery = Delivery.of(name, phoneNumber, address, detailAddress, zoneCode, deliveryMemo);
    }

    // 배송 요청 (운송장 번호 없이 생성)
    public static Shipment requestShipment(Long orderId) {
        Shipment shipment = new Shipment();
        shipment.orderId = orderId;
        shipment.status = ShipmentStatus.SHIP_REQUESTED; // 초기 상태는 READY
        shipment.requestedAt = LocalDateTime.now();
        return shipment;
    }

    public static Shipment of(Long providerId,
                              Long orderId,
                              ShipmentStatus shipmentStatus,
                              String name,
                              String phoneNumber,
                              String address,
                              String detailAddress,
                              String zoneCode,
                              String deliveryMemo) {
        return new Shipment(providerId, orderId, shipmentStatus, name, phoneNumber, address, detailAddress, zoneCode, deliveryMemo);
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
