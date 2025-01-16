package com.emotionalcart.product.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class ReadProductStock {
    @Data
    public static class Request {
        @NotNull
        private Long productId;
        @NotNull
        private Long productOptionId;
        @NotNull
        private Long productOptionDetailId;
    }

    @Data
    public static class Response {
        private Long productId;
        private Long productOptionId;
        private Long productOptionDetailId;
        private Boolean isRequired;
        private int quantity;
    }
}