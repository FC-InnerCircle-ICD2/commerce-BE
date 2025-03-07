package com.emotionalcart.order.presentation.controller.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidOrderResponse {

    private Long productId;

    private List<OrderDetailOption> productOptions;

    public ValidOrderResponse(Long productId, List<OrderDetailOption> orderDetailOptions) {
        this.productId = productId;
        this.productOptions = orderDetailOptions;
    }

    public static ValidOrderResponse of(Long productId, List<OrderDetailOption> orderDetailOptions) {
        return new ValidOrderResponse(productId, orderDetailOptions);
    }

    public static ValidOrderResponse empty() {
        return new ValidOrderResponse();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderDetailOption {

        private Long productOptionId;

        private Long productOptionDetailId;

        public OrderDetailOption(Long productOptionId, Long productOptionDetailId) {
            this.productOptionId = productOptionId;
            this.productOptionDetailId = productOptionDetailId;
        }

        public static OrderDetailOption from(Long productOptionId, Long productOptionDetailId) {
            return new OrderDetailOption(productOptionId, productOptionDetailId);
        }

    }

}
