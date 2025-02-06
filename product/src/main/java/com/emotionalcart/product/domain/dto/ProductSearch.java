package com.emotionalcart.product.domain.dto;

import com.emotionalcart.core.feature.product.SortOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
@AllArgsConstructor
public class ProductSearch {
    private PageRequest pageRequest;
    private SortOption sortOption;
    private Long productId;
    private Long categoryId;
    private String keyword;
    private Float priceMin;
    private Float priceMax;
    private Double rating;
}
