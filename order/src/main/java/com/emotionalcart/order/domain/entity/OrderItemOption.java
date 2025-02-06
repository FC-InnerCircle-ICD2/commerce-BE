package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.dto.CreateOrderItemOption;
import com.emotionalcart.order.domain.generator.IdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class OrderItemOption extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    private Long productOptionId;

    private Long productOptionDetailId;

    public static OrderItemOption createOrderItemOption(OrderItem item, CreateOrderItemOption itemOption) {
        OrderItemOption option = new OrderItemOption();
        option.orderItem = item;
        option.productOptionId = itemOption.getProductOptionId();
        option.productOptionDetailId = itemOption.getProductOptionDetailId();
        return option;
    }

}