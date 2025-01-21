package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.base.BasePageRequest;
import com.emotionalcart.product.application.ProductOptions;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadProducts {
    @Getter
    public static class Request extends BasePageRequest {
        int pageSize = 10;
        int pageNumber = 0;

        @Enumerated(EnumType.STRING)
        private SortOption sortOption;
        private Long productId;
        private Long categoryId;
        private String keyword;
        private Float priceMin;
        private Float priceMax;
        private Double rating;

        // 정렬 필드 및 방향 설정
        public void configureSort() {
            if (sortOption != null) {
                setSort(this.sortOption);
            }
        }
    }

    @Getter
    public enum SortOption {
        PRICE_ASC("price", "ASC"),
        PRICE_DESC("price", "DESC"),
        SALES_DESC("sales", "DESC"),
        CREATE_DESC("createdAt","DESC");

        private final String field;
        private final String direction;

        SortOption(String field, String direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    @Data
    public static class Response {
        private Long productId;
        private String name;
        private String description;
        private Integer price;
        private ReadCategories.Response category;
        private ReadProviders.Response provider;
        private List<ProductOptionResponse> productOptions;
        private Double rating;

        public Response(Product product, List<ProductOptionResponse> productOptions, Double rating) {
            this.productId = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();

            // Category와 Provider를 별도 DTO로 변환
            this.category = product.getCategory() != null ? new ReadCategories.Response(product.getCategory()) : null;
            this.provider = product.getProvider() != null ? new ReadProviders.Response(product.getProvider()) : null;

            this.productOptions = productOptions;
            this.rating = rating;
        }

        public static Page<Response> toResponse(Page<Product> products, ProductOptions productOptions, Map<Long, Double> ratings) {
            Map<Long, List<ProductOptionResponse>> productOptionsMap = productOptions.groupByProductId();

            return products.map(product -> {
                List<ProductOptionResponse> productOptionResponses = productOptionsMap.getOrDefault(product.getId(), List.of());
                Double rating = ratings.getOrDefault(product.getId(), null);
                return new Response(product, productOptionResponses, rating);
            });
        }
    }

    @Data
    public static class ProductOptionResponse {
        private Long id;
        private String name;
        private List<ProductOptionDetailResponse> productOptionDetails;

        public ProductOptionResponse(ProductOption productOption) {
            this.id = productOption.getId();
            this.name = productOption.getName();
        }

        public ProductOptionResponse(ProductOption productOption, List<ProductOptionDetailResponse> productOptionDetails) {
            this.id = productOption.getId();
            this.name = productOption.getName();
            this.productOptionDetails = productOptionDetails;
        }

        public static ProductOptionResponse toResponse(ProductOption productOption, List<ProductOptionDetail> productOptionDetails,
                                                             List<ProductImage> productImages) {
            List<ProductOptionDetailResponse> readProductOptionDetails = productOptionDetails.stream()
                    .map(detail -> ProductOptionDetailResponse.toResponse(detail, productImages))
                    .collect(Collectors.toList());

            return new ProductOptionResponse(productOption, readProductOptionDetails);
        }
    }

    @Data
    public static class ProductOptionDetailResponse {
        private String value;
        private int quantity;
        private int additional_price;
        private List<ProductImageResponse> productImage;

        public ProductOptionDetailResponse(ProductOptionDetail productOptionDetail, List<ProductImageResponse> productImages) {
            this.value = productOptionDetail.getValue();
            this.quantity = productOptionDetail.getQuantity();
            this.additional_price = productOptionDetail.getAdditionalPrice();
            this.productImage = productImages;
        }

        public static ProductOptionDetailResponse toResponse(ProductOptionDetail productOptionDetail, List<ProductImage> images) {
            List<ProductImageResponse> readProductImages = images.stream()
                    .map(ProductImageResponse::new)
                    .collect(Collectors.toList());

            return new ProductOptionDetailResponse(productOptionDetail, readProductImages);
        }
    }

    @Data
    public static class ProductImageResponse {
        private Long id;
        private String url;
        private Integer fileOrder;

        public ProductImageResponse(ProductImage productImage) {
            this.id = productImage.getId();
            this.url = productImage.getFilePath();
            this.fileOrder = productImage.getFileOrder();
        }
    }
}
