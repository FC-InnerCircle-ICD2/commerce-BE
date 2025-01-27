package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.presentation.dto.ReadProducts;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

public interface QueryDslProductRepository {
    Page<Product> findAllProducts(Long productId, Long categoryId, String keyword,
                                  Float priceMin, Float priceMax, Double rating,
                                  SortOption sortOption, PageRequest pageRequest);

    List<ProductOption> findProductOptions(Set<Long> productIds);

    List<ProductOptionDetailWithImages> findProductOptionDetailsWithImages(Set<Long> optionIds);
}
