package com.emotionalcart.shipment.domain.entity;

import com.emotionalcart.shipment.domain.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@EntityListeners(StatusTimestampListener.class)
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
    @StatusTimestamp(ShipmentStatus.SHIP_REQUESTED)
    private LocalDateTime requestedAt;

    /**
     * 배송 시작 시간
     */
    @StatusTimestamp(ShipmentStatus.SHIPPED)
    private LocalDateTime shippedAt;

    /**
     * 배송 완료 시간
     */
    @StatusTimestamp(ShipmentStatus.DELIVERED)
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
        this.requestedAt = LocalDateTime.now();
        this.delivery = Delivery.of(name, phoneNumber, address, detailAddress, zoneCode, deliveryMemo);
    }

    // 배송 요청 (운송장 번호 없이 생성)
    public static Shipment requestShipment(Long orderId) {
        Shipment shipment = new Shipment();
        shipment.orderId = orderId;
        shipment.status = ShipmentStatus.SHIP_REQUESTED;
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

    /**
     * 배송 상태 변경
     * 출고 완료 시에는 운송장 번호 생성
     *
     * @param status 배송 상태
     */
    public void updateStatus(ShipmentStatus status) {
        if (!this.status.canTransitionTo(status)) {
            throw new IllegalStateException(
                String.format("Cannot change status from %s to %s", this.status, status)
            );
        }

        this.status = status;
        if (status == ShipmentStatus.SHIPPED && (this.trackingNumber == null || this.trackingNumber.isEmpty())) {
            this.trackingNumber = generateTrackingNumber();
        }
    }

    /**
     * 운송장 번호 생성
     *
     * @return 운송장 번호
     */
    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}