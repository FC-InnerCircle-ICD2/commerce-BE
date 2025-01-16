package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.banner.ProductBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductBannerRepository extends JpaRepository<ProductBanner, Long> {
    ProductBanner findByBannerIdAndIsDeletedFalse(Long bannerId);
}

