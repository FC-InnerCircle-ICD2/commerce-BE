package com.emotionalcart.order.domain.order.entity;

import com.emotionalcart.order.domain.order.entity.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

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
public class Cart extends AuditableEntity {

    @Id
    @IdGenerator
    private Long id;

    /**
     * 총 주문 금액
     */
    @Embedded
    @Column(nullable = false)
    private Money totalPrice;

    /**
     * 장바구니 담은 일시
     */
    @CreatedDate
    private LocalDateTime cartedAt;

    /**
     * 장바구니 담은 회원 식별자
     */
    @CreatedBy
    private Long cartMemberId;

    /**
     * CartItem과의 연관관계
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

}
