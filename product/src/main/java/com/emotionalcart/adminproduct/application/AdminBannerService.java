package com.emotionalcart.adminproduct.application;

import com.emotionalcart.adminproduct.domain.AdminBannerDataProvider;
import com.emotionalcart.adminproduct.domain.AdminProductDataProvider;
import com.emotionalcart.adminproduct.presentation.dto.*;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.banner.Banner;
import com.emotionalcart.core.feature.banner.BannerImage;
import com.emotionalcart.core.feature.banner.BannerType;
import com.emotionalcart.core.feature.banner.ProductBanner;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.s3.S3Utils;
import com.emotionalcart.s3.config.S3Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBannerService {
    private final AdminBannerDataProvider adminBannerDataProvider;
    private final AdminProductDataProvider adminProductDataProvider;
    private final S3Utils s3Utils;

    @Transactional
    public CreateBannerResponse createBanner(CreateBannerRequest request) {

        // 배너 저장
        Banner banner = request.toBannerEntity();
        Banner savedBanner = adminBannerDataProvider.saveBanner(banner);

        // 배너 아이콘 이미지 저장
        String iconPath = uploadAndCreateIconImage(savedBanner,request.getIconImage());

        // 배너 이미지 저장
        BannerImage bannerImage = uploadAndCreateBannerImage(savedBanner, request.getBannerImage());
        adminBannerDataProvider.saveBannerImage(bannerImage);

        savedBanner.setIconPath(iconPath);
        savedBanner.setBannerImage(bannerImage);

        //상품 배너 저장
        if (request.getType() == BannerType.PRODUCT && request.getProductId() != null) {
            Product product = adminProductDataProvider.findProductById(request.getProductId());
            ProductBanner productBanner = new ProductBanner(
                    product,
                    savedBanner,
                    request.getLinkUrl(),
                    request.getLinkType()
            );
            adminBannerDataProvider.saveProductBanner(productBanner);
        }

        return new CreateBannerResponse(savedBanner.getId());
    }

    private String uploadAndCreateIconImage(Banner banner, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // S3 업로드 후 URL 반환
            return s3Utils.uploadFile(S3Constants.BANNER_DIRECTORY, banner.getId().toString(), file);
        } catch (Exception e) {
            throw new ProductException(ErrorCode.S3_UPLOAD_FAILED);
        }
    }

    private BannerImage uploadAndCreateBannerImage(Banner banner, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            // S3 업로드 후 URL 반환
            String fileUrl = s3Utils.uploadFile(S3Constants.BANNER_DIRECTORY, banner.getId().toString(), file);

            // BannerImage 엔티티 생성 및 반환
            return BannerImage.of(
                    file.getOriginalFilename(),
                    fileUrl,
                    file.getContentType(),
                    file.getSize(),
                    1
            );
        } catch (Exception e) {
            throw new ProductException(ErrorCode.S3_UPLOAD_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public List<ReadBannersResponse> readBanners() {
        List<Banner> banners = adminBannerDataProvider.findAllBanners();

        return banners.stream()
                .map(ReadBannersResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReadBannerDetailResponse readBannerDetail(Long bannerId) {
        Banner banner = adminBannerDataProvider.findBannerById(bannerId);
        ProductBanner productBanner = adminBannerDataProvider.findProductBanner(banner.getId());
        return ReadBannerDetailResponse.toResponse(banner, productBanner);
    }

    @Transactional
    public void deleteBanner(Long bannerId) {
        Banner banner = adminBannerDataProvider.findBannerById(bannerId);
        banner.delete();

        ProductBanner productBanner = adminBannerDataProvider.findProductBanner(bannerId);
        productBanner.delete();
    }

    @Transactional
    public void updateBanner(Long bannerId, UpdateBannerRequest request) {
        Banner banner = adminBannerDataProvider.findBannerById(bannerId);

        banner.update(
            request.getType(),
            request.getTitle(),
            request.getDescription(),
            request.getBannerOrder(),
            request.getStartDate(),
            request.getEndDate()
        );

        if (request.getIconImage() != null) {
            String iconPath = uploadAndCreateIconImage(banner, request.getIconImage());
            banner.updateIconPath(iconPath);
        }

        if (request.getBannerImage() != null) {
            BannerImage newBannerImage = uploadAndCreateBannerImage(banner, request.getBannerImage());
            adminBannerDataProvider.saveBannerImage(newBannerImage);
            banner.updateBannerImage(newBannerImage);
        }

        if (request.getType() == BannerType.PRODUCT && request.getProductId() != null) {
            Product product = adminProductDataProvider.findProductById(request.getProductId());
            ProductBanner productBanner = adminBannerDataProvider.findProductBanner(bannerId);
            productBanner.update(product, banner, request.getLinkUrl(), request.getLinkType());
        }
    }

}
