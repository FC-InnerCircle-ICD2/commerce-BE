package com.emotionalcart.product.domain;

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
    private Long productBannerId;

    @NotNull
    private Long productId;

    private ProductBannerImage(
            Long productBannerId,
            Long productId,
            String bucketName,
            String originalFileName,
            String filePath,
            String fileType,
            Long fileSize,
            Integer fileOrder
    ) {
        super(bucketName, originalFileName, filePath, fileType, fileSize, fileOrder);
        this.productBannerId = productBannerId;
        this.productId = productId;
    }
}
