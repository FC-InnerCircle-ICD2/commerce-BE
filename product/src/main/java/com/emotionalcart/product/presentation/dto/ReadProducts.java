package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.base.BasePageRequest;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.domain.support.ProductOptionDetails;
import com.emotionalcart.product.domain.support.ProductOptions;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.Product;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
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
        private List<ProductOptionResponse> options;
        private Double rating;

        public Response(Product product, List<ProductOptionResponse> options, Double rating) {
            this.productId = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();

            // Category와 Provider를 별도 DTO로 변환
            this.category = product.getCategory() != null ? new ReadCategories.Response(product.getCategory()) : null;
            this.provider = product.getProvider() != null ? new ReadProviders.Response(product.getProvider()) : null;

            this.options = options;
            this.rating = rating;
        }

        public static Page<Response> toResponse(Page<Product> products, Map<Long, List<ProductOptionResponse>> groupedOptions, Map<Long, Double> ratings) {
            return products.map(product -> {
                Long productId = product.getId();
                List<ProductOptionResponse> productOptionResponses = groupedOptions.getOrDefault(productId, List.of());
                Double rating = ratings.getOrDefault(productId, null);

                return new Response(product, productOptionResponses, rating);
            });
        }
    }

    @Data
    public static class ProductOptionResponse {
        @Getter
        private Long id;
        private String name;
        private List<ProductOptionDetailResponse> optionDetails = new ArrayList<>();

        public ProductOptionResponse(ProductOption productOption) {
            this.id = productOption.getId();
            this.name = productOption.getName();
        }

        public void addDetails(List<ProductOptionDetailResponse> details) {
            if (details != null && !details.isEmpty()) {
                this.optionDetails.addAll(details);
            }
        }
    }

    @Data
    public static class ProductOptionDetailResponse {
        private String value;
        private int quantity;
        private int additional_price;
        private Integer fileOrder;
        private String url;

        public ProductOptionDetailResponse (ProductOptionDetailWithImages detailWithImages) {
            this.value = detailWithImages.getValue();
            this.quantity = detailWithImages.getQuantity();
            this.additional_price = detailWithImages.getAdditionalPrice();
            this.fileOrder = detailWithImages.getFileOrder();
            this.url = detailWithImages.getUrl();
        }

        public static ProductOptionDetailResponse toResponse(ProductOptionDetailWithImages detailWithImages) {
            return new ProductOptionDetailResponse(detailWithImages);
        }
    }
}
