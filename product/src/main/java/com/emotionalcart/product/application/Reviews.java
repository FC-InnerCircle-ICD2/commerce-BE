package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.review.Review;

import java.util.List;

public class Reviews {

    private final List<Review> reviews;

    private Reviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public static Reviews from(List<Review> reviews) {
        return new Reviews(reviews);
    }

    public List<Long> ids() {
        return this.reviews.stream()
                .map(Review::getId)
                .toList();
    }

}
