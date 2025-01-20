package com.emotionalcart.product.presentation.dto;

import java.util.List;

import com.emotionalcart.core.feature.product.Product;

import lombok.Data;

public class ReadProductDetails {

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private Integer price;
        private ReadCategories.Response categoryResponse;
        private ReadProviders.Response providerResponse;
        private List<ReadProductOptions.Response> productOptions;
        // private List<ReadProductReviews.Response> productReviews; // 리뷰는 후에 추가
        // private List<ReadProductImages.Response> productImages;

        public Response(Product product,
                List<ReadProductOptions.Response> productOptions, ReadCategories.Response categoryResponse,
                ReadProviders.Response providerResponse) {
            this.id = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();
            this.categoryResponse = categoryResponse;
            this.providerResponse = providerResponse;
            this.productOptions = productOptions;
            // this.productImages = productImages;
        }
    }

    public static Response toResponse(Product product,
            List<ReadProductOptions.Response> productOptions, ReadCategories.Response categoryResponse,
            ReadProviders.Response providerResponse) {
        return new Response(product, productOptions, categoryResponse, providerResponse);
    }
}
