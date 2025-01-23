package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.presentation.dto.ReadProducts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

public interface QueryDslProductRepository {
    Page<Product> findAllProducts(ReadProducts.Request request, PageRequest pageRequest);

    List<ProductOption> findProductOptions(Set<Long> productIds);

    List<ProductOptionDetailWithImages> findProductOptionDetailsWithImages(Set<Long> optionIds);
}
