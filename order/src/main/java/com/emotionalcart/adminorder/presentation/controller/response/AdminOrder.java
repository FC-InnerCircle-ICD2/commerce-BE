package com.emotionalcart.adminorder.presentation.controller.response;

import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.enums.OrderStatus;
import com.emotionalcart.order.domain.enums.PaymentMethod;
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

    public AdminOrder(Orders orders) {
        this.orderId = orders.getId();
        this.paymentMethod = orders.getPaymentMethod().getMethodName();
        this.orderStatus = orders.getStatus().getStatusName();
        this.totalPrice = orders.getTotalPrice();
        this.orderAt = orders.getOrderAt();
    }

    public AdminOrder(Long orderId, PaymentMethod paymentMethod, OrderStatus orderStatus, LocalDateTime orderAt, double totalPrice) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod.getMethodName();
        this.orderStatus = orderStatus.getStatusName();
        this.orderAt = orderAt;
        this.totalPrice = totalPrice;
    }

    public static AdminOrder fromEntity(Orders orders) {
        return new AdminOrder(orders.getId(), orders.getPaymentMethod(), orders.getStatus(), orders.getOrderAt(), orders.getTotalPrice());
    }

}
