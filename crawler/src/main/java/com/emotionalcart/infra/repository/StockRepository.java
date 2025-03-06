package com.emotionalcart.infra.repository;

import com.emotionalcart.domain.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByProduct_Id(Long productId);

}
