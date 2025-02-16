package com.emotionalcart.order.infra.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 상세 응답 값
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailResponse {

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private ProductProvider provider;
    private List<ProductDetailImages> images;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductDetailImages {

        private Long id;
        private Integer fileOrder;
        private String url;
        private ProductImageType type;

        public enum ProductImageType {
            MAIN, DETAIL;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductProvider {

        private Long id;
        private String name;

    }

}
