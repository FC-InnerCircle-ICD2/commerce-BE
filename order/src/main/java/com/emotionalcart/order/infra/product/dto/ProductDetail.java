package com.emotionalcart.order.infra.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetail {

    private Long productId;

    private String productName;

    private Long providerId;

    private String providerName;

    private String productImage;

    public ProductDetail(ProductDetailResponse response) {
        this.productId = response.getId();
        this.productName = response.getName();
        this.providerId = response.getProvider().getId();
        this.providerName = response.getProvider().getName();
        this.productImage =
            response.getImages().stream().filter(image -> image.getType().toString().equals("MAIN")).map(ProductDetailResponse.ProductDetailImages::getUrl).findFirst().orElse(
                "");

    }

    public static ProductDetail from(ProductDetailResponse responseBody) {
        return new ProductDetail(responseBody);
    }

}
