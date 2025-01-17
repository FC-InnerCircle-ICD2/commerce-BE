package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.BannerType;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class ReadBanners {

    @Data
    public static class Response {
        private Long id;
        private BannerType type;
        private String title;
        private Integer bannerOrder;
        private String iconUrl;
        private ReadProductBanners.Response productBannerResponse;

        public Response(Banner banner) {
            this.id = banner.getId();
            this.type = banner.getType();
            this.title = banner.getTitle();
            this.bannerOrder = banner.getBannerOrder();
            this.iconUrl = banner.getIconPath();
        }

        public Response(Banner banner, ReadProductBanners.Response productBannerResponse) {
            this.id = banner.getId();
            this.type = banner.getType();
            this.title = banner.getTitle();
            this.bannerOrder = banner.getBannerOrder();
            this.iconUrl = banner.getIconPath();
            this.productBannerResponse = productBannerResponse;
        }

        public static List<Response> toResponse(List<Banner> banners) {
            return banners.stream().map(Response::new).collect(Collectors.toList());
        }
    }
}
