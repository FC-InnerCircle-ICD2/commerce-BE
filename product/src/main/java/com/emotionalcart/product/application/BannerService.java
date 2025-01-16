package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.product.presentation.dto.ReadBanners;
import com.emotionalcart.product.domain.BannerDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerDataProvider bannerDataProvider;

    public List<ReadBanners.Response> getBanners(){
        List<Banner> banners = bannerDataProvider.findAllBanners();

        return Banners.from(banners, bannerDataProvider).getBanners();
    }
}
