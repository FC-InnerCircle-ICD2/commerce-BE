package com.emotionalcart.core.feature.banner;

import com.emotionalcart.core.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_image_id")
    private BannerImage bannerImage;

    @NotNull
    @Enumerated(EnumType.STRING)
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

    @NotNull
    private String iconPath;

    private Banner(
            Long id,
            BannerImage bannerImage,
            BannerType type,
            String title,
            String description,
            Integer bannerOrder,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String iconPath
    ) {
        this.id = id;
        this.bannerImage = bannerImage;
        this.type = type;
        this.title = title;
        this.description = description;
        this.bannerOrder = bannerOrder;
        this.startDate = startDate;
        this.endDate = endDate;
        this.iconPath = iconPath;
    }
}
