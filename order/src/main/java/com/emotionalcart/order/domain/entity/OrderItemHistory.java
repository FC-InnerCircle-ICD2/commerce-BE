package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 항목 내역
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemHistory extends AuditableEntity {

    @Id
    @IdGenerator
    private Long id;

    /**
     * 상품 식별자
     */
    private Long productId;

    /**
     * 상품명
     */
    private String productName;

    /**
     * 결제 금액
     */
    @Embedded
    @Column(nullable = false)
    private Money orderItemPrice;

    /**
     * 수량
     */
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    public static OrderItemHistory createOrderItemHistory(OrderItem orderItem) {
        OrderItemHistory orderItemHistory = new OrderItemHistory();
        orderItemHistory.productId = orderItem.getProductId();
        orderItemHistory.productName = orderItem.getProductName();
        orderItemHistory.orderItemPrice = orderItem.getOrderItemPrice();
        orderItemHistory.quantity = orderItem.getQuantity();
        orderItemHistory.orderItem = orderItem;
        return orderItemHistory;
    }

}
