package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.common.generator.IdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 주문 항목 내역
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemHistory {

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
    private int orderItemPrice;

    /**
     * 수량
     */
    private int quantity;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

}
