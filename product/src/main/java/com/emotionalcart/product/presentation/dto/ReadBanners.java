package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.banner.*;

import lombok.Data;

public class ReadBanners {

    @Data
    public static class Response {
        private Long id;
        private BannerType type;
        private String title;
        private Integer bannerOrder;
        private String iconUrl;
        private BannerImageResponse bannerImage;
        private ReadProductBanners.Response productBannerResponse;

        public Response(Banner banner, BannerImageResponse bannerImage) {
            this.id = banner.getId();
            this.type = banner.getType();
            this.title = banner.getTitle();
            this.bannerOrder = banner.getBannerOrder();
            this.iconUrl = banner.getIconPath();
            this.bannerImage = bannerImage;
        }

        public Response(Response response, ReadProductBanners.Response productBannerResponse) {
            this.id = response.getId();
            this.type = response.getType();
            this.title = response.getTitle();
            this.bannerOrder = response.getBannerOrder();
            this.iconUrl = response.getIconUrl();
            this.bannerImage = response.getBannerImage();
            this.productBannerResponse = productBannerResponse;
        }

        public static Response toResponse(Banner banner, BannerImage bannerImage) {
            BannerImageResponse imageResponse = (bannerImage != null) ? new BannerImageResponse(bannerImage) : null;
            return new Response(banner, imageResponse);
        }
    }

    @Data
    public static class BannerImageResponse {
        private Long id;
        private String url;
        private Integer fileOrder;

        public BannerImageResponse(BannerImage bannerImage) {
            this.id = bannerImage.getId();
            this.url = bannerImage.getFilePath();
            this.fileOrder = bannerImage.getFileOrder();
        }
    }
}
