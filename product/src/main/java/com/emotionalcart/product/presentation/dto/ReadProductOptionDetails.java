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

        private Response(ProductOptionDetail optionDetail, List<ReadProductImages.Response> images) {
            this.id = optionDetail.getId();
            this.value = optionDetail.getValue();
            this.quantity = optionDetail.getQuantity();
            this.order = optionDetail.getOptionOrder();
            this.additionalPrice = optionDetail.getAdditionalPrice();
            this.images = images;
        }

        public static Response toResponse(ProductOptionDetail optionDetail, List<ProductImage> images) {
            List<ReadProductImages.Response> imagesResponse = images.stream()
                    .map(ReadProductImages.Response::new)
                    .collect(Collectors.toList());

            return new Response(optionDetail, imagesResponse);
        }
    }
}
