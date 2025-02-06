package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.entity.OrderItem;
import lombok.Getter;

/**
 * 주문 상세 항목
 */
@Getter
public class DetailOrderItem {

    /**
     * 상품 아이디
     */
    private final Long productId;

    /**
     * 상품 이름
     */
    private final String productName;

    /**
     * 결제 금액
     */
    private final double orderItemPrice;

    /**
     * 수량
     */
    private final int quantity;

    public DetailOrderItem(OrderItem orderItem) {
        this.productId = orderItem.getProductId();
        this.productName = orderItem.getProductName();
        this.orderItemPrice = orderItem.getItemPrice();
        this.quantity = orderItem.getQuantity();
    }

    public static DetailOrderItem from(OrderItem orderItem) {
        return new DetailOrderItem(orderItem);
    }

}
