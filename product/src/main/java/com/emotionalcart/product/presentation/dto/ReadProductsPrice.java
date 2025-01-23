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
        private List<ProductOption> productOptions;

        private Response(Long productId, Integer price, List<ProductOption> productOptions) {
            this.productId = productId;
            this.price = price;
            this.productOptions = productOptions;
        }

        @Getter
        @Setter
        public static class ProductOption {
            private Long productOptionId;
            private Long productOptionDetailId;
            private Integer additionalPrice;

            private ProductOption(Long productOptionId, Long productOptionDetailId, Integer additionalPrice) {
                this.productOptionId = productOptionId;
                this.productOptionDetailId = productOptionDetailId;
                this.additionalPrice = (additionalPrice != null) ? additionalPrice : 0;
            }

            public static ProductOption fromProductDetail(ProductDetail productDetail) {
                return new ProductOption(
                        productDetail.getProductOptionId(),
                        productDetail.getProductOptionDetailId(),
                        productDetail.getProductAdditionalPrice()
                );
            }
        }
    }

    public static Response toResponse(Long productId, List<ProductDetail> productDetails) {
        Integer productPrice = productDetails.getFirst().getProductPrice();
        List<ReadProductsPrice.Response.ProductOption> productOptions = productDetails.stream()
                .map(Response.ProductOption::fromProductDetail)
                .toList();

        return new Response(productId, productPrice, productOptions);
    }
}
