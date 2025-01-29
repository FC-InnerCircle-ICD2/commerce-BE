package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, QueryDslStockRepository {

}
