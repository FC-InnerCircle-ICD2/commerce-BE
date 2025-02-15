package com.emotionalcart.stock.domain;

import com.emotionalcart.generator.IdGenerator;
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

    private int additionalPrice;

    private int optionOrder;

    public static ProductOptionDetail of(Long id) {
        ProductOptionDetail detail = new ProductOptionDetail();
        detail.id = id;
        return detail;
    }

}
