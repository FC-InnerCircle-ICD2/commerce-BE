package com.emotionalcart.core.feature.banner;

import com.emotionalcart.core.base.BaseImageEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductBannerImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_banner_id")
    private ProductBanner productBanner;

    private ProductBannerImage(
            ProductBanner productBanner,
            String bucketName,
            String originalFileName,
            String filePath,
            String fileType,
            Long fileSize,
            Integer fileOrder
    ) {
        super(bucketName, originalFileName, filePath, fileType, fileSize, fileOrder);
        this.productBanner = productBanner;
    }
}
