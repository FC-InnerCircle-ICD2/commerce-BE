package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 항목
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    /**
     * 상품 아이디
     */
    @Column(nullable = false)
    private Long productId;

    /**
     * 상품 이름
     */
    @Column(nullable = false)
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
    @Column(nullable = false)
    private int quantity;

    public static OrderItem createOrderItem(Orders orders, CreateOrderItem orderItem) {
        OrderItem item = new OrderItem();
        item.orders = orders;
        item.productId = orderItem.getProductId();
        item.productName = orderItem.getProductName();
        item.orderItemPrice = Money.of(orderItem.getPrice());
        item.quantity = orderItem.getQuantity();
        return item;
    }

}