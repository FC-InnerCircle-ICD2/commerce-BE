package com.emotionalcart.product.infrastructure.persistence;

import com.emotionalcart.product.domain.entity.ProductCategory;
import com.emotionalcart.product.domain.repository.ProductCategoryRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductCategoryRespositoryImpl implements ProductCategoryRespository {

    private final JpaProductCategoryRepository jpaProductCategoryRepository;

    @Override
    public Page<ProductCategory> findAll(Pageable pageable) {
        return jpaProductCategoryRepository.findAll(pageable);
    }

//    @Override
//    public ProductCategory save(ProductCategory productCategory) {
//        return jpaProductCategoryRepository.save(productCategory);
//    }
}
