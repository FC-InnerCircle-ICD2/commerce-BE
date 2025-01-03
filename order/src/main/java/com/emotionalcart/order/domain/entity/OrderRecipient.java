package com.emotionalcart.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table
public class OrderRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 주문과의 관계 (1:1)
     */
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * 수신자명
     */
    @Column(nullable = false, length = 50)
    private String recipientsName;

    /**
     * 수신자 전화번호
     */
    @Column(nullable = false, length = 11)
    private String recipientsPhone;

    /**
     * 우편번호
     */
    @Column(nullable = false, length = 10)
    private String postalCode;

    /**
     * 기본주소
     */
    @Column(nullable = false, length = 50)
    private String defaultAddress;

    /**
     * 상세주소
     */
    @Column(nullable = false, length = 100)
    private String detailAddress;

    /**
     * 주문요청사항
     */
    @Column(length = 50)
    private String orderRequirement;

    /**
     * 배송요청사항
     */
    @Column(length = 50)
    private String deliveryRequirement;

}
