package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.base.BasePageRequest;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.product.SortOption;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.product.domain.dto.ProductSearch;
import com.emotionalcart.product.domain.support.ProductImages;
import com.emotionalcart.product.domain.support.ProductOptions;
import lombok.*;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadProducts {

    @Getter
    @Setter
    public static class Request extends BasePageRequest {
        private SortOption sortOption;
        private Long productId;
        private Long categoryId;
        private String keyword;
        private Float priceMin;
        private Float priceMax;
        private Double rating;

        public ProductSearch toProductSearch() {
            return new ProductSearch(
               getPageable(),
               sortOption,
               productId,
               categoryId,
               keyword,
               priceMin,
               priceMax,
               rating
            );
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
        private List<ReadProductImages.Response> images;

        public Response(Product product, List<ProductOptionResponse> options, Category category, Provider provider, List<ReadProductImages.Response> images) {
            this.productId = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();
            this.category = category != null ? new ReadCategories.Response(category) : null;
            this.provider = provider != null ? new ReadProviders.Response(provider) : null;
            this.options = options;
            this.rating = product.getReviewStatistic() != null ? product.getReviewStatistic().getAverageRating() : null;
            this.images = images;
        }

        public static Page<Response> toResponse(Page<Product> products, ProductOptions productOptions,
                                                Map<Long, Category> categoryMap,
                                                Map<Long, Provider> providerMap,
                                                ProductImages productImages) {
            Map<Long,List<ProductOptionResponse>> optionsMap = productOptions.groupByProductId();
            Map<Product, List<ReadProductImages.Response>> readProductImagesMap = productImages.groupByProductId();

            return products.map(product -> {
                Long productId = product.getId();
                List<ProductOptionResponse> productOptionResponses = optionsMap.getOrDefault(productId, List.of());
                Category category = categoryMap.getOrDefault(product.getCategoryId(), null);
                Provider provider = providerMap.getOrDefault(product.getProviderId(), null);
                //Long sales = salesData.getOrDefault(product.getId(), 0L); // 판매량 정보 포함
                List<ReadProductImages.Response> images = readProductImagesMap.getOrDefault(product, List.of());

                return new Response(product, productOptionResponses, category, provider, images);
            });
        }
    }

    @Data
    public static class ProductOptionResponse {
        @Getter
        private Long id;
        private String name;
        private List<ProductOptionDetailResponse> optionDetails;

        public ProductOptionResponse(ProductOption productOption, List<ProductOptionDetailResponse> details) {
            this.id = productOption.getId();
            this.name = productOption.getName();
            this.optionDetails = details;
        }

        public static ProductOptionResponse toResponse(ProductOption productOption) {
            List<ProductOptionDetailResponse> details = productOption.getDetails().stream()
                    .map(ProductOptionDetailResponse::toResponse)
                    .collect(Collectors.toList());

            return new ProductOptionResponse(productOption, details);
        }
    }

    @Data
    public static class ProductOptionDetailResponse {
        private Long id;
        private String value;
        private Integer quantity;
        private Integer order;
        private Integer additionalPrice;

        private ProductOptionDetailResponse(ProductOptionDetail optionDetail) {
            this.id = optionDetail.getId();
            this.value = optionDetail.getValue();
            this.order = optionDetail.getOptionOrder();
            this.additionalPrice = optionDetail.getAdditionalPrice();
        }

        public static ProductOptionDetailResponse toResponse(ProductOptionDetail optionDetail) {
            return new ProductOptionDetailResponse(optionDetail);
        }
    }
}
