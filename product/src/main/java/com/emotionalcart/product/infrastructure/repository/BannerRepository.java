package com.emotionalcart.product.infrastructure.repository;

import com.emotionalcart.core.feature.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long>, QueryDslBannerRepository {

}
