package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.banner.ProductBanner;
import com.emotionalcart.core.feature.banner.ProductBannerImage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadProductBanners {

    @Data
    public static class Response {
        private Long id;
        private String linkUrl;
        private String linkType;
        private ProductBannerImageResponse image;

        public Response(ProductBanner productBanner, ProductBannerImageResponse image ) {
            this.id = productBanner.getId();
            this.linkUrl = productBanner.getLinkUrl();
            this.linkType = productBanner.getLinkType();
            this.image = image;
        }

        public static Response toResponse(ProductBanner productBanner, ProductBannerImage productBannerImage) {
            ProductBannerImageResponse imageResponse = (productBannerImage != null) ? new ProductBannerImageResponse(productBannerImage) : null;
            return new Response(productBanner, imageResponse);
        }
    }

    @Data
    public static class ProductBannerImageResponse {
        private Long id;
        private String url;
        private Integer fileOrder;

        public ProductBannerImageResponse(ProductBannerImage productBannerImage) {
            this.id = productBannerImage.getId();
            this.url = productBannerImage.getFilePath();
            this.fileOrder = productBannerImage.getFileOrder();
        }
    }
}
