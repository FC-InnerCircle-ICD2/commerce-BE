package com.emotionalcart.adminproduct.presentation.dto;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.BannerType;
import com.emotionalcart.core.feature.banner.ProductBanner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadBannerDetailResponse {
    private Long id;
    private String title;
    private String description;
    private Integer bannerOrder;
    private String iconUrl;
    private String bannerImageUrl;
    private BannerType bannerType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private ReadProductBanners productBannerResponse;

    public ReadBannerDetailResponse(Banner banner, ReadProductBanners productBannerResponse) {
        this.id = banner.getId();
        this.title = banner.getTitle();
        this.description = banner.getDescription();
        this.bannerOrder = banner.getBannerOrder();
        this.iconUrl = banner.getIconPath();
        this.bannerImageUrl = banner.getBannerImage() != null ? banner.getBannerImage().getFilePath() : null;
        this.bannerType = banner.getType();
        this.startDate = banner.getStartDate();
        this.endDate = banner.getEndDate();
        this.createdAt = banner.getCreatedAt();
        this.updatedAt = banner.getUpdatedAt();
        this.isDeleted = banner.getIsDeleted();
        this.productBannerResponse = productBannerResponse;
    }

    public static ReadBannerDetailResponse toResponse(Banner banner, ProductBanner productBanner) {
        return new ReadBannerDetailResponse(banner, ReadProductBanners.toResponse(productBanner));
    }

    @Data
    public static class ReadProductBanners {
        private Long id;
        private String linkUrl;
        private String linkType;
        private Long productId;

        public ReadProductBanners(ProductBanner productBanner) {
            this.id = productBanner.getId();
            this.linkUrl = productBanner.getLinkUrl();
            this.linkType = productBanner.getLinkType();
            this.productId = productBanner.getProduct().getId();
        }

        public static ReadProductBanners toResponse(ProductBanner productBanner) {
            return new ReadProductBanners(productBanner);
        }
    }
}
