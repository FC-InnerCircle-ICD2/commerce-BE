package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.base.BasePageRequest;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.product.SortOption;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.product.domain.dto.ProductSearch;
import com.emotionalcart.product.domain.support.ProductImages;
import com.emotionalcart.product.domain.support.ProductOptions;
import com.emotionalcart.product.infrastructure.elasticsearch.dto.ElasticProduct;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
        @JsonSerialize(using = ToStringSerializer.class)
        private Long productId;
        private String name;
        private String description;
        private Integer price;
        private ReadCategories.Response category;
        private ReadProviders.Response provider;
        private List<ProductOptionResponse> options;
        private Double rating;
        private List<ReadProductImages.Response> images;
        //private int totalStockQuantity;

        //DB 응답
        public Response(Product product, List<ProductOptionResponse> options, Category category, Provider provider, List<ReadProductImages.Response> images ) {
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

        //Elastic 응답
        public Response(ElasticProduct product, List<ProductOptionResponse> options, Category category, Provider provider, Double rating, List<ReadProductImages.Response> images ) {
            this.productId = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();
            this.category = category != null ? new ReadCategories.Response(category) : null;
            this.provider = provider != null ? new ReadProviders.Response(provider) : null;
            this.options = options;
            this.rating = rating;
            this.images = images;
        }

        //DB 응답
        public static Page<Response> toResponse(Page<Product> products, ProductOptions productOptions,
                                                Map<Long, Category> categoryMap,
                                                Map<Long, Provider> providerMap,
                                                ProductImages productImages) {
            Map<Long,List<ProductOptionResponse>> optionsMap = productOptions.groupByProductId();
            Map<Product, List<ReadProductImages.Response>> readProductImagesMap = productImages.groupByProduct();

            return products.map(product -> {
                Long productId = product.getId();
                List<ProductOptionResponse> productOptionResponses = optionsMap.getOrDefault(productId, List.of());
                Category category = categoryMap.getOrDefault(product.getCategoryId(), null);
                Provider provider = providerMap.getOrDefault(product.getProviderId(), null);
                List<ReadProductImages.Response> images = readProductImagesMap.getOrDefault(product, List.of());

                return new Response(product, productOptionResponses, category, provider, images);
            });
        }

        //Elastic 응답
        public static Page<Response> toResponse(Page<ElasticProduct> products,
                                                Map<Long, Category> categoryMap,
                                                Map<Long, Provider> providerMap,
                                                Map<Long, Double> ratingMap,
                                                ProductImages productImages) {
            Map<Long, List<ReadProductImages.Response>> readProductImagesMap = productImages.groupByProductId();

            return products.map(product -> {
                Long productId = product.getId();
                List<ProductOptionResponse> productOptionResponses = product.getOptions().stream().map(option -> {
                    List<ProductOptionDetailResponse> details = option.getDetails().stream()
                        .map(detail -> new ProductOptionDetailResponse(detail.getId(),
                                                                       detail.getOptionDetailName(),
                                                                       detail.getOptionOrder(),
                                                                       detail.getAdditionalPrice()))
                        .toList();

                    return new ProductOptionResponse(option.getId(), option.getOptionName(), details);
                }).toList();
                Category category = categoryMap.getOrDefault(product.getCategoryId(), null);
                Provider provider = providerMap.getOrDefault(product.getProviderId(), null);
                Double rating = ratingMap.getOrDefault(productId, null);
                List<ReadProductImages.Response> images = readProductImagesMap.getOrDefault(productId, List.of());

                return new Response(product, productOptionResponses, category, provider, rating, images);
            });
        }
    }

    @Data
    public static class ProductOptionResponse {
        @Getter
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String name;
        private List<ProductOptionDetailResponse> optionDetails;

        public ProductOptionResponse(ProductOption productOption, List<ProductOptionDetailResponse> details) {
            this.id = productOption.getId();
            this.name = productOption.getName();
            this.optionDetails = details;
        }

        public ProductOptionResponse(Long id, String optionName, List<ProductOptionDetailResponse> details) {
            this.id = id;
            this.name = optionName;
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
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String value;
        private Integer order;
        private Integer additionalPrice;

        private ProductOptionDetailResponse(ProductOptionDetail optionDetail) {
            this.id = optionDetail.getId();
            this.value = optionDetail.getValue();
            this.order = optionDetail.getOptionOrder();
            this.additionalPrice = optionDetail.getAdditionalPrice();
        }

        public ProductOptionDetailResponse(Long id, String optionDetailName, Integer order, Integer additionalPrice) {
            this.id = id;
            this.value = optionDetailName;
            this.order = order;
            this.additionalPrice = additionalPrice;
        }

        public static ProductOptionDetailResponse toResponse(ProductOptionDetail optionDetail) {
            return new ProductOptionDetailResponse(optionDetail);
        }
    }
}
