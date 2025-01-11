package com.emotionalcart.order.domain.dto;

import lombok.Getter;

/**
 * 생성된 주문
 */
@Getter
public class CreatedOrder {

    private Long id;

    public static CreatedOrder defaultOrder() {
        CreatedOrder createdOrder = new CreatedOrder();
        createdOrder.id = 1L;
        return createdOrder;
    }

}
