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
    private List<ProductOption> options;
    private List<ProductDetailImages> images;

    /**
     * 상품 이미지
     */
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

    /**
     * 상품 업체
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductProvider {

        private Long id;
        private String name;

    }

    /**
     * 상품 옵션
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductOption {

        private Long id;

        private String name;

        private List<ProductOptionDetail> optionDetails;

    }

    /***
     * 상품 옵션 상세
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductOptionDetail {

        private Long id;
        private String value;
        private Integer order;
        private Integer additionalPrice;

    }

}
