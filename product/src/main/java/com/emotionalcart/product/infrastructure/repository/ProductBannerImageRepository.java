package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.banner.ProductBannerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBannerImageRepository extends JpaRepository<ProductBannerImage, Long> {
    ProductBannerImage findByProductBannerIdAndIsDeletedFalse(Long productBannerId);
}
