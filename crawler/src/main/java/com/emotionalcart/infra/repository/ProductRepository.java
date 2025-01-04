package com.emotionalcart.infra.repository;

import com.emotionalcart.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
