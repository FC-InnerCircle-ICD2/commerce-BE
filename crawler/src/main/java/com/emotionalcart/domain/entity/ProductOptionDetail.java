package com.emotionalcart.domain.entity;

import com.emotionalcart.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionDetail {

    @Id
    @IdGenerator
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    private String value;

    private String imageUrl;

    private int additionalPrice;

    private int optionOrder;

    public static ProductOptionDetail of(String name, ProductOption productOption) {
        ProductOptionDetail detail = new ProductOptionDetail();
        detail.value = name;
        detail.productOption = productOption;
        detail.additionalPrice = getRandomNumber();
        detail.optionOrder = productOption.getProductOptionDetails().size();
        productOption.addDetail(detail);
        return detail;
    }

    public static ProductOptionDetail of(String name, String imageUrl, ProductOption productOption) {
        ProductOptionDetail detail = new ProductOptionDetail();
        detail.value = name;
        detail.imageUrl = imageUrl;
        detail.productOption = productOption;
        detail.additionalPrice = getRandomNumber();
        detail.optionOrder = productOption.getProductOptionDetails().size();
        productOption.addDetail(detail);
        return detail;
    }

    private static int getRandomNumber() {
        int random = ThreadLocalRandom.current().nextInt(1000, 10001); // 1000 ~ 10000
        return (random / 100) * 100; // 100 단위 절삭
    }

    public static ProductOptionDetail of(Long optionDetailId) {
        ProductOptionDetail detail = new ProductOptionDetail();
        detail.id = optionDetailId;
        return detail;
    }

}
