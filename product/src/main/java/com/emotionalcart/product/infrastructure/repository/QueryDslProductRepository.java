package com.emotionalcart.product.infrastructure.repository;


import com.emotionalcart.product.presentation.dto.ReadProductStock;

import java.util.Optional;

public interface QueryDslProductRepository {
    Optional<ReadProductStock.Response> findProductsStock(ReadProductStock.Request request);
}
