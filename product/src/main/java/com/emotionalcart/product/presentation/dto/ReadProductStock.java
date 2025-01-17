package com.emotionalcart.product.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ReadProductStock {
    @Getter
    public static class Request {
        @NotNull
        private Long productId;
        @NotNull
        private Long productOptionId;
        @NotNull
        private Long productOptionDetailId;
    }

    @Getter
    @Setter
    public static class Response {
        private Long productId;
        private Long productOptionId;
        private Long productOptionDetailId;
        private Boolean isRequired;
        private int quantity;
    }
}