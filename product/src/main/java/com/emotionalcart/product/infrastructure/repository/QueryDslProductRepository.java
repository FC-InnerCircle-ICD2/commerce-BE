package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.domain.dto.ProductSearch;
import org.springframework.data.domain.Page;
import com.emotionalcart.product.domain.dto.ProductDetail;

import java.util.List;
import java.util.Set;

public interface QueryDslProductRepository {
    Page<Product> findAllProducts(ProductSearch productSearch);

    List<ProductOption> findProductOptions(Set<Long> productIds);

    List<ProductOptionDetailWithImages> findProductOptionDetailsWithImages(Set<Long> optionIds);

    List<ProductDetail> findAllProductDetail(Set<Long> productIds);
}
