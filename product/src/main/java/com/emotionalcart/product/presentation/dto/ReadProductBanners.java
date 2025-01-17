package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.banner.ProductBanner;
import lombok.Data;

public class ReadProductBanners {

    @Data
    public static class Response {
        private Long id;
        private String linkUrl;
        private String linkType;

        public Response(ProductBanner productBanner) {
            this.id = productBanner.getId();
            this.linkUrl = productBanner.getLinkUrl();
            this.linkType = productBanner.getLinkType();
        }

        public static Response toResponse(ProductBanner productBanner) {
            return new Response(productBanner);
        }
    }
}
