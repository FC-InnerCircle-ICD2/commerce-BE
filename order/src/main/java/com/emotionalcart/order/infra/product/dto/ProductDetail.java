package com.emotionalcart.order.infra.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetail {

    private Long productId;

    private String productName;

    private Long providerId;

    private String providerName;

    private String productImage;

    private List<ProductDetailOption> productOptions;

    public ProductDetail(ProductDetailResponse response) {
        this.productId = response.getId();
        this.productName = response.getName();
        this.providerId = response.getProvider().getId();
        this.providerName = response.getProvider().getName();
        this.productImage =
            response.getImages().stream().filter(image -> image.getType().toString().equals("MAIN")).map(ProductDetailResponse.ProductDetailImages::getUrl).findFirst().orElse(
                "");

        this.productOptions = response.getOptions().stream().map(ProductDetailOption::from).toList();

    }

    public static ProductDetail from(ProductDetailResponse responseBody) {
        return new ProductDetail(responseBody);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductDetailOption {

        private Long id;

        private String name;

        private List<ProductOptionDetail> optionDetails;

        public ProductDetailOption(Long id, String name, List<ProductDetailResponse.ProductOptionDetail> optionDetails) {
            this.id = id;
            this.name = name;
            this.optionDetails = optionDetails.stream().map(ProductOptionDetail::from).toList();
        }

        public static ProductDetailOption from(ProductDetailResponse.ProductOption po) {
            return new ProductDetailOption(po.getId(), po.getName(), po.getOptionDetails());
        }

        public void updateOptionDetail(Long optionItem) {
            this.optionDetails = this.optionDetails.stream()
                .filter(detail -> optionItem.equals(detail.getId()))
                .toList();
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductOptionDetail {

        private Long id;
        private String value;
        private Integer order;
        private Integer additionalPrice;

        public ProductOptionDetail(Long id, String value, Integer order, Integer additionalPrice) {
            this.id = id;
            this.value = value;
            this.order = order;
            this.additionalPrice = additionalPrice;
        }

        public static ProductOptionDetail from(ProductDetailResponse.ProductOptionDetail od) {
            return new ProductOptionDetail(od.getId(), od.getValue(), od.getOrder(), od.getAdditionalPrice());
        }

    }

}
