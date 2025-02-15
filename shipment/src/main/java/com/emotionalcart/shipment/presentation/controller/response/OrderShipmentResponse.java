package com.emotionalcart.shipment.presentation.controller.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderShipmentResponse {

    private Long orderId;
    private String deliveryStatus;
    private String trackingNumber;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    public OrderShipmentResponse(Long orderId,
                                 String deliveryStatus,
                                 String trackingNumber,
                                 LocalDateTime shippedAt,
                                 LocalDateTime deliveredAt) {
        this.orderId = orderId;
        this.deliveryStatus = deliveryStatus;
        this.trackingNumber = trackingNumber;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
    }

    public static OrderShipmentResponse of(Long orderId,
                                           String deliveryStatus,
                                           String trackingNumber,
                                           LocalDateTime shippedAt,
                                           LocalDateTime deliveredAt) {

        return new OrderShipmentResponse(orderId, deliveryStatus, trackingNumber, shippedAt, deliveredAt);
    }

}
