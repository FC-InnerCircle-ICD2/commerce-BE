package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.common.generator.IdGenerator;
import com.emotionalcart.order.domain.enums.OrderStatus;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 테이블
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @IdGenerator
    private Long id;

    /**
     * 결제수단
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    /**
     * 총 주문금액
     */
    @Embedded
    @Column(nullable = false)
    private Price totalPrice;

    /**
     * 주문일시
     */
    @Column(nullable = false)
    private LocalDateTime orderAt;

    /**
     * 주문 회원 식별자
     */
    @CreatedBy
    private Long orderMemberId;

    /**
     * 주문상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * 주문 항목 목록
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    /**
     * 수신자 정보
     */
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderRecipient orderRecipient;

}
