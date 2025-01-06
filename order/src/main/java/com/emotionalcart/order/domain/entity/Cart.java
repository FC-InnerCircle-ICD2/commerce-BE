package com.emotionalcart.order.domain.entity;

import com.emotionalcart.order.common.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 장바구니 테이블
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @IdGenerator
    private Long id;

    /**
     * 총 주문 금액
     */
    @Column(nullable = false)
    private int totalPrice;

    /**
     * 주문일시
     */
    @CreatedDate
    private LocalDateTime orderedAt;

    /**
     * 주문 회원 식별자
     */
    @CreatedBy
    private Long orderMemberId;

    /**
     * 생성일시
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 만료일시
     */
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    /**
     * CartItem과의 연관관계
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

}
