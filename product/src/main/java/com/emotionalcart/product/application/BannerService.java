package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.BannerImage;
import com.emotionalcart.core.feature.banner.ProductBanner;
import com.emotionalcart.product.presentation.dto.ReadBanners;
import com.emotionalcart.product.domain.BannerDataProvider;
import com.emotionalcart.product.presentation.dto.ReadProductBanners;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerDataProvider bannerDataProvider;

    public List<ReadBanners.Response> getBanners(){
        List<Banner> banners = bannerDataProvider.findAllBanners();

        return banners.stream()
                .map(this::mapBannerToResponse)
                .toList();
    }

    private ReadBanners.Response mapBannerToResponse(Banner banner) {

        ReadBanners.Response response = ReadBanners.Response.toResponse(banner);

        return switch (banner.getType()) {
            case PRODUCT -> fromProductBanner(response);   // 상품 배너 처리
            //case EVENT -> fromEventBanner(response);     // 이벤트 배너 처리
            default -> response;                           // 기본 배너 처리
        };
    }

    private ReadBanners.Response fromProductBanner(ReadBanners.Response response) {

        // 상품 배너 데이터 조회
        ProductBanner productBanner = bannerDataProvider.findProductBanner(response.getId());

        // productBanner가 null인 경우 처리
        if (productBanner == null) {
            return response;
        }

        ReadProductBanners.Response productResponse = ReadProductBanners.Response.toResponse(productBanner);

        return new ReadBanners.Response(response, productResponse);
    }
}
