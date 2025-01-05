package com.emotionalcart.order.domain.entity;

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
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "order_id")
    private Order order;

    /**
     * 상품 아이디
     */
    @Column(nullable = false)
    private Long productId;

    /**
     * 상품 옵션 아이디
     */
    @Column(nullable = false)
    private Long productOptionsId;

    /**
     * 최종 가격 : 주문 갯수 + 단가
     */
    @Column(nullable = false)
    private Integer totalPrice;

    /**
     * 주문 항목 별 가격
     */
    @Column(nullable = false)
    private Integer orderItemPrice;

}