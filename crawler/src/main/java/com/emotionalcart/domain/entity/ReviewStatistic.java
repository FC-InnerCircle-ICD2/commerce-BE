package com.emotionalcart.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.security.SecureRandom;

@Getter
@Entity
public class ReviewStatistic extends BaseEntity {

    private static final SecureRandom random = new SecureRandom();
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private double averageRating;

    private int totalRating;

    private int reviewCount;

    public static ReviewStatistic of(Product product) {
        ReviewStatistic reviewStatistic = new ReviewStatistic();
        reviewStatistic.product = product;
        reviewStatistic.averageRating = getRandomRating();
        reviewStatistic.totalRating = getRandomInteger();
        reviewStatistic.reviewCount = getRandomInteger();
        return reviewStatistic;
    }

    private static double getRandomRating() {
        int integerPart = random.nextInt(6); // 0~5 정수 부분
        int decimalPart = random.nextInt(2); // 0 또는 1 (소수점 첫째 자리 포함 여부 결정)

        if (decimalPart == 1) {
            double fraction = Math.round(random.nextDouble() * 10.0) / 10.0; // 0.0~1.0 중 랜덤한 소수점 첫째 자리 값
            double result = integerPart + fraction;
            return Math.min(result, 5.0); // 5.0을 초과하지 않도록 보정
        }
        return integerPart;
    }

    public static int getRandomInteger() {
        return random.nextInt(1001); // 0~1000 사이의 랜덤 정수
    }

}
