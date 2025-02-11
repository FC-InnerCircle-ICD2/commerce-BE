package com.emotionalcart.product.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateReviewStatisticEvent {
    private final Long productId;
    private final Integer rating;
}
