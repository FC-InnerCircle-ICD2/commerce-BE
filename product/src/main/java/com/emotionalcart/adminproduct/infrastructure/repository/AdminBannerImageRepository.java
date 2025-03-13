package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.core.feature.banner.BannerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBannerImageRepository  extends JpaRepository<BannerImage, Long> {
}
