package com.emotionalcart.order.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class CreateOrderItemOption extends SelfValidation<CreateOrderItemOption> {

    /**
     * 상품 옵션 식별자
     */
    @NotNull(message = "상품 옵션을 선택해주세요.")
    private Long productOptionId;

    /**
     * 상품 옵션 상세 식별자
     */
    @NotNull(message = "상품 옵션 상세를 선택해주세요.")
    private Long productOptionDetailId;

    /**
     * 추가 금액
     */
    private double additionalPrice;

}
