package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.entity.OrderAddress;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.domain.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class OrderInfo {

    /**
     * 주문번호
     */
    private Long orderId;

    /**
     * 배송상태
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * 수신자명
     */
    private String recipientName;

    /**
     * 수신자 전화번호
     */
    private String recipientPhone;

    /**
     * 주소 정보
     */
    private OrderAddress address;

    public OrderInfo(Orders orders) {
        this.orderId = orders.getId();
        this.status = orders.getStatus();
        this.recipientName = orders.getRecipientName();
        this.recipientPhone = orders.getRecipientPhone();
        this.address = orders.getRecipientAddress();
    }

    public static OrderInfo fromEntity(Orders order) {
        return new OrderInfo(order);
    }

}
