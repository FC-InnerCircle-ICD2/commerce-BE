package com.emotionalcart.stock.domain.repository;

import com.emotionalcart.stock.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
