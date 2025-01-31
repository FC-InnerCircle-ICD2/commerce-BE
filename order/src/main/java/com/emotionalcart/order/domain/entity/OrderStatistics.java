package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.dto.CreateOrderItem;
import com.emotionalcart.order.domain.generator.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 주문 통계 테이블
 */
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatistics extends AuditableEntity {

    @Id
    @IdGenerator
    private Long id;

    /**
     * 상품 아이디
     */
    @Column(nullable = false)
    private Long productId;

    /**
     * 카테고리 식별자
     * 카테고리 식별자는 최하위 자식 카테고리만 저장
     */
    @Column(nullable = false)
    private Long categoryId;

    /**
     * 주문 건 수
     */
    private Long totalOrder;

    /**
     * 총 판매 횟 수
     */
    private Long totalQuantitySold;

    /**
     * 마지막 주문 시간
     */
    @LastModifiedDate
    private LocalDateTime lastOrderedAt;

    public OrderStatistics(CreateOrderItem orderItem) {
        this.productId = orderItem.getProductId();
        this.categoryId = orderItem.getCategoryId();
        this.totalOrder = 0L;
        this.totalQuantitySold = 0L;
    }

    public static OrderStatistics create(CreateOrderItem orderItem) {
        return new OrderStatistics(orderItem);
    }

    public void updateOrderStatistics(CreateOrderItem orderItem) {
        this.totalOrder++;
        this.totalQuantitySold += orderItem.getQuantity();
    }

}
