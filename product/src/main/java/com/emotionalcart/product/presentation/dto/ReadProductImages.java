package com.emotionalcart.product.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.emotionalcart.core.feature.product.ProductImage;

import lombok.Data;

public class ReadProductImages {

    @Data
    public static class Response {
        private Long id;
        private boolean isRepresentative;
        private Integer fileOrder;
        private String url;

        public Response(ProductImage productImage) {
            this.id = productImage.getId();
            this.isRepresentative = productImage.isRepresentative();
            this.fileOrder = productImage.getFileOrder();
            this.url = productImage.getFilePath();
        }

        public static Response toResponse(ProductImage productImage) {
            return new Response(productImage);
        }

        public static List<Response> toResponse(List<ProductImage> productImages) {
            return productImages.stream()
                    .map(Response::new)
                    .collect(Collectors.toList());
        }
    }

}
