package com.emotionalcart.core.feature.product;

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
public class ProductImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_detail_id")
    private ProductOptionDetail productOptionDetail;

    // @NotNull
    // private Long productId;

    @NotNull
    private boolean isRepresentative;

    private ProductImage(
            boolean isRepresentative,
            String bucketName,
            String originalFileName,
            String filePath,
            String fileType,
            Long fileSize,
            Integer fileOrder
    ) {
        super(bucketName, originalFileName, filePath, fileType, fileSize, fileOrder);
        this.isRepresentative = isRepresentative;
    }
}
