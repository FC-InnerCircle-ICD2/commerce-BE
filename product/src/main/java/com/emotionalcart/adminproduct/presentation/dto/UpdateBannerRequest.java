package com.emotionalcart.adminproduct.presentation.dto;

import com.emotionalcart.core.feature.banner.BannerType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateBannerRequest {
    private BannerType type;

    private String title;

    private String description;

    private Integer bannerOrder;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private MultipartFile iconImage;

    private Long productId;
    private String linkUrl;
    private String linkType;

    private MultipartFile bannerImage;
}
