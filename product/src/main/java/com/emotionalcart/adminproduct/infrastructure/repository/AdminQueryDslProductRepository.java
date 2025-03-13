package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.adminproduct.infrastructure.AdminProducts;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AdminQueryDslProductRepository {

    Page<AdminProducts> findAllProducts(PageRequest pageRequest);

    void deleteProductOptions(Product product, List<Long> productOptionIds);
    void deleteProductOptionDetails(List<ProductOption> options, List<Long> productOptionDetailIds);

}
