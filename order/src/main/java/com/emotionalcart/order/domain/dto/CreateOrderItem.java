package com.emotionalcart.order.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * <h2>주문 상품 생성 DTO</h2>
 */
@Getter
@Builder
public class CreateOrderItem extends SelfValidation<CreateOrderItem> {

    /**
     * 상품 식별자
     */
    @NotNull(message = "상품을 선택해주세요.")
    private Long productId;

    /**
     * 상품 옵션 식별자
     */
    @NotNull(message = "상품 옵션을 선택해주세요.")
    private Long productOptionId;

    /**
     * 상품명
     */
    @NotNull(message = "상품명을 입력해주세요.")
    private String productName;

    /**
     * 상품 금액
     */
    @Min(value = 100, message = "상품 금액은 100원 이상이어야 합니다.")
    private double price;

    /**
     * 수량
     */
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;

}
