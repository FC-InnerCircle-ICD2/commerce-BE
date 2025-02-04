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

        public Response(ProductImage image) {
            this.id = image.getId();
            this.isRepresentative = image.isRepresentative();
            this.fileOrder = image.getFileOrder();
            this.url = image.getFilePath();
        }

        public static Response toResponse(ProductImage image) {
            return new Response(image);
        }

        public static List<Response> toResponse(List<ProductImage> images) {
            return images.stream()
                    .map(Response::new)
                    .collect(Collectors.toList());
        }
    }

}
