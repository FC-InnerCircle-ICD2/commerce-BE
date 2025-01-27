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
        private ReadProductCategories.Response category;
        private ReadProviders.Response provider;
        private List<ReadProductOptions.Response> options;
        private ReadProductReviewStatistic.Response reviewStatistic;

        public Response(Product product,
                List<ReadProductOptions.Response> options, ReadProductCategories.Response categoryResponse,
                ReadProviders.Response providerResponse, ReadProductReviewStatistic.Response reviewStatistic) {
            this.id = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();
            this.category = categoryResponse;
            this.provider = providerResponse;
            this.options = options;
            this.reviewStatistic = reviewStatistic;
        }

        public static Response toResponse(Product product,
                List<ReadProductOptions.Response> options, ReadProductCategories.Response categoryResponse,
                ReadProviders.Response providerResponse, ReadProductReviewStatistic.Response reviewStatistic) {
            return new Response(product, options, categoryResponse, providerResponse, reviewStatistic);
        }
    }
}
