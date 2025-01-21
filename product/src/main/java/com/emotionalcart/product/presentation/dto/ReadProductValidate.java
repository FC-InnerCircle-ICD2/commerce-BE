package com.emotionalcart.product.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class ReadProductValidate {
    @Getter
    public static class Request {
        @NotNull
        private Long productId;
        @NotNull
        private List<OptionRequest> productOptions;

        @Getter
        public static class OptionRequest {
            @NotNull
            private Long productOptionId;
            @NotNull
            private Long productOptionDetailId;
            @NotNull
            private Integer quantity;
        }
    }
}