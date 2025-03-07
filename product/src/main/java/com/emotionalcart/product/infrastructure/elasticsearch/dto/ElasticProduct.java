package com.emotionalcart.product.infrastructure.elasticsearch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ElasticProduct {

    @Id
    private Long id;

    private String name;

    private String description;

    private Integer price;

    private Long providerId;

    private String providerName;

    private Long categoryId;

    private String categoryName;

    private List<ProductOptionField> options;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime createdAt;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductOptionField {
        private Long id;

        private String optionName;

        private List<ProductOptionDetailField> details;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductOptionDetailField {
        private Long id;

        private String optionDetailName;

        private Integer optionOrder;

        private Integer additionalPrice;
    }
}
