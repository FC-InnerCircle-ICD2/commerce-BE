package com.emotionalcart.core.feature.banner;

import com.emotionalcart.core.base.BaseEntity;
import com.emotionalcart.core.feature.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductBanner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Banner banner;

    @NotNull
    private String linkUrl;

    @NotNull
    private String linkType; //internal/external

    public ProductBanner(
            Long id,
            Product product,
            Banner banner,
            String linkUrl,
            String linkType
    ) {
        this.id = id;
        this.product = product;
        this.banner = banner;
        this.linkUrl = linkUrl;
        this.linkType = linkType;
    }
}
