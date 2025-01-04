package com.emotionalcart.domain.entity;

import com.emotionalcart.domain.generator.IdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private int quantity;

    public static ProductOptionDetail of(String name, ProductOption productOption) {
        ProductOptionDetail detail = new ProductOptionDetail();
        detail.value = name;
        detail.productOption = productOption;
        productOption.addDetail(detail);
        return detail;
    }

    public static ProductOptionDetail of(String name, String imageUrl, ProductOption productOption) {
        ProductOptionDetail detail = new ProductOptionDetail();
        detail.value = name;
        detail.imageUrl = imageUrl;
        detail.productOption = productOption;
        productOption.addDetail(detail);
        return detail;
    }

}
