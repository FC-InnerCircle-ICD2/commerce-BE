package com.emotionalcart.order.domain.order.entity;

import com.emotionalcart.order.domain.order.entity.generator.IdGenerator;
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
    private Long productName;

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

}