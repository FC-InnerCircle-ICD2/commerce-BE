package com.emotionalcart.shipment.domain.dto;

import com.emotionalcart.shipment.presentation.controller.request.OrderShipmentRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class CreateShipment {

    private List<Long> providerIds;
    private Long orderId;
    private String name;
    private String phoneNumber;
    private String address;
    private String detailAddress;
    private String zoneCode;
    private String deliveryMemo;

    protected CreateShipment(OrderShipmentRequest request) {
        this.orderId = request.getOrderId();
        this.providerIds = request.getProviderIds();
        this.name = request.getDeliveryInfo().getName();
        this.phoneNumber = request.getDeliveryInfo().getPhoneNumber();
        this.address = request.getDeliveryInfo().getAddress();
        this.detailAddress = request.getDeliveryInfo().getDetailAddress();
        this.zoneCode = request.getDeliveryInfo().getZoneCode();
        this.deliveryMemo = request.getDeliveryInfo().getDeliveryMemo();
    }

    public static CreateShipment from(OrderShipmentRequest orderShipmentRequest) {
        return new CreateShipment(orderShipmentRequest);
    }

}
