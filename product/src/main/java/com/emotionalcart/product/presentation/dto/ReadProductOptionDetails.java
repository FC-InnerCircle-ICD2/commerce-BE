package com.emotionalcart.product.presentation.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOptionDetail;

import lombok.Data;

public class ReadProductOptionDetails {

    @Data
    public static class Response {
        private Long id;
        private String value;
        private Integer quantity;
        private Integer order;
        private Integer additionalPrice;
        private List<ReadProductImages.Response> images;

        private Response(ProductOptionDetail productOptionDetail, List<ReadProductImages.Response> images) {
            this.id = productOptionDetail.getId();
            this.value = productOptionDetail.getValue();
            this.quantity = productOptionDetail.getQuantity();
            this.order = productOptionDetail.getOrder();
            this.additionalPrice = productOptionDetail.getAdditionalPrice();
            this.images = images;
        }
    }

    public static Response toResponse(ProductOptionDetail productOptionDetail, List<ProductImage> images) {
        List<ReadProductImages.Response> readProductImages = images.stream()
                .map(ReadProductImages::toResponse)
                .collect(Collectors.toList());

        return new Response(productOptionDetail, readProductImages);
    }

}
