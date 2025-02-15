package com.emotionalcart.adminproduct.presentation.dto;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.BannerType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateBannerRequest {
    @NotNull
    private BannerType type;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Integer bannerOrder;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    private MultipartFile iconImage;

    private Boolean isDeleted;

    // 상품 배너 관련 필드
    private Long productId;
    private String linkUrl;
    private String linkType; // internal/external

    // 이미지 관련 필드
    private MultipartFile bannerImage;

    public Banner toBannerEntity() {
        return Banner.of(
                type,
                title,
                description,
                bannerOrder,
                startDate,
                endDate
        );
    }
}
