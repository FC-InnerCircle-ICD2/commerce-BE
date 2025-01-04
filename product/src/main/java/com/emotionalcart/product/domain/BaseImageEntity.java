package com.emotionalcart.product.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseImageEntity extends BaseEntity {

    @NotNull
    private String bucketName;

    @NotNull
    private String originalFileName;

    @NotNull
    private String filePath;

    @NotNull
    private String fileType;

    @NotNull
    private Long fileSize;

    @NotNull
    private Integer fileOrder;

    protected BaseImageEntity(
            String bucketName,
            String originalFileName,
            String filePath,
            String fileType,
            Long fileSize,
            Integer fileOrder
    ) {
        this.bucketName = bucketName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileOrder = fileOrder;
    }
}
