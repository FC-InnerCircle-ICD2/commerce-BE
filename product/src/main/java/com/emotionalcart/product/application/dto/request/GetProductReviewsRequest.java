package com.emotionalcart.product.application.dto.request;

import lombok.Data;

@Data
public class GetProductReviewsRequest {
    int pageSize = 10;
    int pageNumber = 0;
}
