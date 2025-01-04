package com.emotionalcart.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends com.emotionalcart.product.domain.BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long reviewId;

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

    private ReviewImage(
            Long reviewId,
            String bucketName,
            String originalFileName,
            String filePath,
            String fileType,
            Long fileSize,
            Integer fileOrder
    ) {
        this.reviewId = reviewId;
        this.filePath = filePath;
        this.bucketName = bucketName;
        this.originalFileName = originalFileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileOrder = fileOrder;
    }

    public static ReviewImage of(
            Long reviewId,
            String bucketName,
            String originalFileName,
            String filePath,
            String fileType,
            Long fileSize,
            Integer fileOrder
    ) {
        return new ReviewImage(
                reviewId,
                bucketName,
                originalFileName,
                filePath,
                fileType,
                fileSize,
                fileOrder
        );
    }
}