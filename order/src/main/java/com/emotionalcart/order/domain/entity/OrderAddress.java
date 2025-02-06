package com.emotionalcart.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
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

    public static OrderAddress createNewOrderAddress(@NotNull(message = "우편번호를 입력해주세요.") String zonecode,
                                                     @NotNull(message = "주소를 입력해주세요.") String address,
                                                     @NotNull(message = "상세주소를 입력해주세요.") String detailAddress) {
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.postalCode = zonecode;
        orderAddress.defaultAddress = address;
        orderAddress.detailAddress = detailAddress;
        return orderAddress;
    }

}
