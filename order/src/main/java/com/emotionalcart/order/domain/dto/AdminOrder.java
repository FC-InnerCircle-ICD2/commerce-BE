package com.emotionalcart.order.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminOrder {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    private String paymentMethod;

    private String orderStatus;

    private LocalDateTime orderAt;

    private double totalPrice;

    public AdminOrder(Long orderId, String paymentMethod, String orderStatus, LocalDateTime orderAt, double totalPrice) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.orderAt = orderAt;
        this.totalPrice = totalPrice;
    }

    public static AdminOrder of(Long orderId, String paymentMethod, String orderStatus, LocalDateTime orderAt, double totalPrice) {
        return new AdminOrder(orderId, paymentMethod, orderStatus, orderAt, totalPrice);
    }

}
