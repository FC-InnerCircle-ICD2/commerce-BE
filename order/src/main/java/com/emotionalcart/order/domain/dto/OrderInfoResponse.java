package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.entity.OrderAddress;
import com.emotionalcart.order.domain.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
public class OrderInfoResponse {

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
     * 주소
     */
    private OrderAddress recipientAddress;

    public static OrderInfoResponse from(OrderInfo orderInfo) {
        OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
        copyProperties(orderInfo, orderInfoResponse);
        return orderInfoResponse;
    }

}
