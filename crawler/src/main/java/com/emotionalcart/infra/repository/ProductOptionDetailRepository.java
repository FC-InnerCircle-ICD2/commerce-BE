package com.emotionalcart.infra.repository;

import com.emotionalcart.domain.entity.ProductOptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionDetailRepository extends JpaRepository<ProductOptionDetail, Long> {

}
