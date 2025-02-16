package com.emotionalcart.stock.domain.repository;

import com.emotionalcart.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryQuerydsl {

    List<Stock> findByProduct_Id(Long productId);

}
