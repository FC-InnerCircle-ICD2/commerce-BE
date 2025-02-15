package com.emotionalcart.shipment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String zoneCode;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    /**
     * 배송 메모는 최대 50까지만 입력 가능
     */
    @Column(length = 50)
    private String deliveryMemo;

    public Delivery(String name, String phoneNumber, String address, String detailAddress, String zoneCode, String deliveryMemo) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zoneCode = zoneCode;
        this.deliveryMemo = deliveryMemo;
    }

    public static Delivery of(String name, String phoneNumber, String address, String detailAddress, String zoneCode, String deliveryMemo) {
        return new Delivery(name, phoneNumber, address, detailAddress, zoneCode, deliveryMemo);
    }

}
