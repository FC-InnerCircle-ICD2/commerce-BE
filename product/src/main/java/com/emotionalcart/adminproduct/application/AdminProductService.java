package com.emotionalcart.adminproduct.application;

import com.emotionalcart.adminproduct.domain.AdminCategoryDataProvider;
import com.emotionalcart.adminproduct.domain.AdminProductDataProvider;
import com.emotionalcart.adminproduct.domain.AdminProviderDataProvider;
import com.emotionalcart.adminproduct.presentation.dto.CreateProductRequest;
import com.emotionalcart.adminproduct.presentation.dto.CreateProductResponse;
import com.emotionalcart.adminproduct.presentation.dto.ReadAdminProductDetailResponse;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductImageType;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.core.feature.review.ReviewStatistic;
import com.emotionalcart.s3.S3Utils;
import com.emotionalcart.s3.config.S3Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductService {
    private final AdminProductDataProvider adminProductDataProvider;
    private final AdminProviderDataProvider adminProviderDataProvider;
    private final AdminCategoryDataProvider adminCategoryDataProvider;
    private final S3Utils s3Utils;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        // category, provider 유효성 체크
        adminProviderDataProvider.findProviderById(request.getProviderId());
        adminCategoryDataProvider.validateCategory(request.getCategoryId());

        Product product = request.toEntity();

        // 리뷰 평점 기본값 저장
        ReviewStatistic reviewStatistic = ReviewStatistic.of(product);
        product.setReviewStatistic(reviewStatistic);

        // 상품 저장
        Product savedProduct = adminProductDataProvider.saveProduct(product);

        // 상품 이미지 저장
        ProductImage mainProductImage = uploadAndSaveProductImage(savedProduct, ProductImageType.MAIN, request.getMainImage(), 1);
        List<ProductImage> detailProductImages = IntStream.range(0, request.getDetailImages().size())
                .mapToObj(i -> uploadAndSaveProductImage(savedProduct, ProductImageType.DETAIL, request.getDetailImages().get(i), i + 1))
                .toList();
        List<ProductImage> productImages = new ArrayList<>();
        productImages.add(mainProductImage);
        productImages.addAll(detailProductImages);
        savedProduct.setImages(productImages);

        return new CreateProductResponse(savedProduct.getId());
    }

    /**
     * 상품 이미지 entity 생성 및 s3 저장
     */
    private ProductImage uploadAndSaveProductImage(Product product, ProductImageType productImageType, MultipartFile file, Integer fileOrder) {
        try {
            String fileUrl = s3Utils.uploadFile(S3Constants.PRODUCT_DIRECTORY, product.getId().toString(), file);
            return ProductImage.of(
                    product,
                    productImageType,
                    S3Constants.BUCKET_NAME,
                    file.getOriginalFilename(),
                    fileUrl,
                    file.getContentType(),
                    file.getSize(),
                    fileOrder
            );
        } catch (Exception e) {
            throw new ProductException(ErrorCode.S3_UPLOAD_FAILED);
        }
    }

    /**
     * 상품 상세 조회
     */
    public ReadAdminProductDetailResponse readProduct(Long productId) {
        Product product = adminProductDataProvider.findProductById(productId);
        Provider provider = adminProviderDataProvider.findProviderById(product.getProviderId());
        Category category = adminCategoryDataProvider.findCategory(product.getCategoryId());
        return ReadAdminProductDetailResponse.toResponse(product, category, provider);
    }

    /**
     * 상품 삭제 (논리삭제 isDeleted = ture)
     */
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = adminProductDataProvider.findProductById(productId);
        // TODO provider 확인 필요해보임
        product.delete();
    }
}