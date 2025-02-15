package com.emotionalcart.stock.domain;

import com.emotionalcart.stock.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProductImage extends BaseEntity {

    @Id
    @IdGenerator
    private Long id;

    private String filePath = "images/";

    private String fileType = "image/jpeg";

    private Long fileSize = 0L;

    private int fileOrder;

    private String bucketName = "coupang";

    private String originalFileName = "default.jpg";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    public static ProductImage of(Product product, String imageUrl, ImageType imageType) {
        ProductImage productImage = new ProductImage();
        productImage.product = product;
        productImage.originalFileName = imageUrl;
        productImage.imageType = imageType;
        return productImage;
    }

    public void markOrder(int size) {
        this.fileOrder = size;
    }

}
