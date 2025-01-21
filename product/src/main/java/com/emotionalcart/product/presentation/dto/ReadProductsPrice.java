package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.product.domain.dto.ProductDetail;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ReadProductsPrice {
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
        }
    }

    @Getter
    @Setter
    public static class Response {
        private Long productId;
        private Integer price;
        private Long productOptionId;
        private Long productOptionDetailId;
        private Integer additionalPrice;

        public Response(ProductDetail productDetail) {
            this.productId = productDetail.getProductId();
            this.price = productDetail.getProductPrice();
            this.productOptionId = productDetail.getProductOptionId();
            this.productOptionDetailId = productDetail.getProductOptionDetailId();
            this.additionalPrice = productDetail.getProductAdditionalPrice();
        }
    }
}