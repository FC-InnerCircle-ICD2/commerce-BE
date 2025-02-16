package com.emotionalcart.stock.domain.repository;

import com.emotionalcart.stock.domain.StockOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOptionRepository extends JpaRepository<StockOption, Long> {

}
