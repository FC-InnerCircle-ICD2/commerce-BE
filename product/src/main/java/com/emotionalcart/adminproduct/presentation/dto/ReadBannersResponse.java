package com.emotionalcart.adminproduct.presentation.dto;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.BannerType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadBannersResponse {
    private Long id;
    private String title;
    private Integer bannerOrder;
    private BannerType bannerType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    public ReadBannersResponse(Banner banner) {
        this.id = banner.getId();
        this.title = banner.getTitle();
        this.bannerOrder = banner.getBannerOrder();
        this.bannerType = banner.getType();
        this.startDate = banner.getStartDate();
        this.endDate = banner.getEndDate();
        this.createdAt = banner.getCreatedAt();
        this.updatedAt = banner.getUpdatedAt();
        this.isDeleted = banner.getIsDeleted();
    }

    public static ReadBannersResponse toResponse(Banner banner) {
        return new ReadBannersResponse(banner);
    }
}
