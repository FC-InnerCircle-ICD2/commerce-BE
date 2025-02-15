package com.emotionalcart.product.presentation.dto;

import java.util.List;

import com.emotionalcart.core.feature.product.Product;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

public class ReadProductDetails {

    @Data
    public static class Response {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String name;
        private String description;
        private Integer price;
        private ReadProductCategories.Response category;
        private ReadProviders.Response provider;
        private List<ReadProductOptions.Response> options;
        private ReadProductReviewStatistic.Response reviewStatistic;
        private List<ReadProductImages.Response> images;

        public Response(Product product,
                List<ReadProductOptions.Response> options, ReadProductCategories.Response categoryResponse,
                ReadProviders.Response providerResponse, ReadProductReviewStatistic.Response reviewStatistic,
                List<ReadProductImages.Response> images) {
            this.id = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();
            this.category = categoryResponse;
            this.provider = providerResponse;
            this.options = options;
            this.reviewStatistic = reviewStatistic;
            this.images = images;
        }

        public static Response toResponse(Product product,
                List<ReadProductOptions.Response> options, ReadProductCategories.Response categoryResponse,
                ReadProviders.Response providerResponse, ReadProductReviewStatistic.Response reviewStatistic,
                List<ReadProductImages.Response> images) {
            return new Response(product, options, categoryResponse, providerResponse, reviewStatistic, images);
        }
    }
}
