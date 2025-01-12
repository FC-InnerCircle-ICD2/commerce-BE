package com.emotionalcart.order.domain.order.entity;

import com.emotionalcart.order.domain.order.entity.generator.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

}
