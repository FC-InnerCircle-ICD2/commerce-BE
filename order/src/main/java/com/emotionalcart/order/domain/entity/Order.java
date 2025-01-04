package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.domain.enums.OrderStatus;
import com.emotionalcart.order.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(nullable = false)
    private Double totalPrice;

    /**
     * 주문일시
     */
    @Column(nullable = false)
    private LocalDateTime orderAt;

    /**
     * 삭제여부
     */
    @Column(nullable = false)
    private Boolean isDeleted;

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
