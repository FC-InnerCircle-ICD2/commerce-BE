package com.emotionalcart.order.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 주소
 *
 * @author yeji cho
 * @since 2025.1.5
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAddress {

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

}
