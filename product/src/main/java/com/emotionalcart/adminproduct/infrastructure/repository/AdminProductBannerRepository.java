package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.core.feature.banner.ProductBanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminProductBannerRepository extends JpaRepository<ProductBanner, Long> {
    Optional<ProductBanner> findByBannerId(Long bannerId);
}
