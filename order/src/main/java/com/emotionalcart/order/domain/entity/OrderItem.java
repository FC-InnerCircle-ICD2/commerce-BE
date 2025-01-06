package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.common.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

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
    @IdGenerator
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

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
    @Column(nullable = false)
    private int orderItemPrice;

    /**
     * 수량
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 생성일시
     */
    @CreatedDate
    private LocalDateTime createdAt;
}