package com.emotionalcart.product.application.dto.response;

import com.emotionalcart.product.domain.ReviewImage;
import lombok.Data;

@Data
public class ReviewImageResponse {
    private Long id;
    private String url;
    private Integer fileOrder;

    public ReviewImageResponse(ReviewImage reviewImage) {
        this.id = reviewImage.getId();
        this.url = reviewImage.getFilePath();
        this.fileOrder = reviewImage.getFileOrder();
    }
}
