package com.emotionalcart.adminproduct.infrastructure.repository;

import com.emotionalcart.core.feature.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBannerRepository extends JpaRepository<Banner, Long> {
}
