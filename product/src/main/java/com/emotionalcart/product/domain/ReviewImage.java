package com.emotionalcart.product.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "review_image")
@Getter
@Setter
@NoArgsConstructor
public class ReviewImage extends com.emotionalcart.product.domain.BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "bucket_name", nullable = false, length = 100)
    private String bucketName;

    @Column(name = "original_file_name", nullable = false, length = 260)
    private String originalFileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_order", nullable = false)
    @Comment("파일 순서")
    private Integer fileOrder;

    public ReviewImage(
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
}