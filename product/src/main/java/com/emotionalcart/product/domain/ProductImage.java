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
public class ProductImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productOptionId;

    @NotNull
    private Long productId;

    @NotNull
    private boolean isRepresentative;

    private ProductImage(
            Long productOptionId,
            Long productId,
            boolean isRepresentative,
            String bucketName,
            String originalFileName,
            String filePath,
            String fileType,
            Long fileSize,
            Integer fileOrder
    ) {
        super(bucketName, originalFileName, filePath, fileType, fileSize, fileOrder);
        this.productOptionId = productOptionId;
        this.productId = productId;
        this.isRepresentative = isRepresentative;
    }
}
