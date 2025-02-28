package com.emotionalcart.adminproduct.application;

import com.emotionalcart.adminproduct.domain.AdminCategoryDataProvider;
import com.emotionalcart.adminproduct.domain.AdminProductDataProvider;
import com.emotionalcart.adminproduct.domain.AdminProviderDataProvider;
import com.emotionalcart.adminproduct.infrastructure.AdminProducts;
import com.emotionalcart.adminproduct.presentation.dto.*;
import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.category.Category;
import com.emotionalcart.core.feature.product.*;
import com.emotionalcart.core.feature.provider.Provider;
import com.emotionalcart.core.feature.review.ReviewStatistic;
import com.emotionalcart.s3.S3Utils;
import com.emotionalcart.s3.config.S3Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private ProductImage uploadAndSaveProductImage(Product product,
                                                   ProductImageType productImageType,
                                                   MultipartFile file,
                                                   Integer fileOrder) {
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

    public Page<ReadAdminProductsResponse> readProducts(ReadAdminProductsRequest request) {
        Page<AdminProducts> products = adminProductDataProvider.findAllProduct(request.getPageable());
        List<ReadAdminProductsResponse> responseList = products.getContent().stream()
            .map(this::mapToResponse)
            .toList();
        return new PageImpl<>(responseList, request.getPageable(), products.getTotalElements());
    }

    private ReadAdminProductsResponse mapToResponse(AdminProducts product) {
        return new ReadAdminProductsResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getCategoryName(),
            product.getProviderName(),
            product.getMainImageUrl()
        );
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

    /**
     * 상품 수정
     */
    @Transactional
    public void updateProduct(Long productId, UpdateProductRequest request) {
        Product product = adminProductDataProvider.findProductById(productId);
        product.updateBasicInfo(request.getName(), request.getPrice(), request.getDescription());
        for (OptionUpdateRequest optionRequest : request.getOptions()) {
            ProductOption option = updateOrCreateProductOption(product, optionRequest);
            updateOrCreateProductOptionDetail(option, optionRequest);
        }
        handleOptionDeletions(product, request);
        handleOptionDetailDeletions(product, request);
        handleProductImageUpdates(product, request);
    }

    /**
     * 상품 이미지 수정
     */
    private void handleProductImageUpdates(Product product, UpdateProductRequest request) {
        if (request.getMainImage() != null) {
            product.deleteMainImage();
            ProductImage mainImage = uploadAndSaveProductImage(product, ProductImageType.MAIN, request.getMainImage(), 1);
            product.addImage(mainImage);
        }

        if (request.getDetailImages() != null && !request.getDetailImages().isEmpty()) {
            int nextOrder = product.getNextDetailImageOrder();
            for (MultipartFile image : request.getDetailImages()) {
                ProductImage detailImage = uploadAndSaveProductImage(product, ProductImageType.DETAIL, image, nextOrder++);
                product.addImage(detailImage);
            }
        }

        if (request.getDeletedImageIds() != null && !request.getDeletedImageIds().isEmpty()) {
            product.deleteDetailImages(request.getDeletedImageIds());
        }
    }

    /**
     * 옵션 수정 / 생성
     */
    private ProductOption updateOrCreateProductOption(Product product, OptionUpdateRequest optionRequest) {
        ProductOption option;

        if (optionRequest.getId() != null) {
            option = product.getOptions().stream()
                .filter(o -> o.getId().equals(optionRequest.getId()))
                .findFirst()
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION));
        } else {
            if (optionRequest.getOptionDetails() == null || optionRequest.getOptionDetails().isEmpty()) {
                throw new ProductException(ErrorCode.AT_LEAST_ONE_OPTION_DETAIL_REQUIRED);
            }
            option = product.addOption(ProductOption.of(optionRequest.getName()));
        }

        return option;
    }

    /**
     * 옵션 상세 수정 / 생성
     */
    private void updateOrCreateProductOptionDetail(ProductOption option, OptionUpdateRequest optionRequest) {
        for (OptionDetailUpdateRequest detailRequest : optionRequest.getOptionDetails()) {
            if (detailRequest.getId() != null) {
                ProductOptionDetail detail = option.getDetails().stream()
                    .filter(d -> d.getId().equals(detailRequest.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_PRODUCT_OPTION_DETAIL));
                detail.updateValue(detailRequest.getValue(), detailRequest.getAdditionalPrice());
            } else {
                option.addDetail(ProductOptionDetail.of(detailRequest.getValue(),
                                                        detailRequest.getOptionOrder(),
                                                        detailRequest.getAdditionalPrice(),
                                                        option));
            }
        }
    }

    /**
     * 상품 옵션 삭제
     * 최소 1개의 옵션이 남아있어야 함
     */
    private void handleOptionDeletions(Product product, UpdateProductRequest request) {
        List<Long> deletedOptionIds = request.getDeletedOptionIds();

        if (deletedOptionIds != null && !deletedOptionIds.isEmpty()) {
            long remainingOptions = product.getOptions().stream()
                .filter(option -> !option.getIsDeleted() && !deletedOptionIds.contains(option.getId())) // 삭제할 옵션 제외
                .count();
            if (remainingOptions <= 0) {
                throw new ProductException(ErrorCode.AT_LEAST_ONE_OPTION_REQUIRED);
            }
            adminProductDataProvider.deleteProductOptions(product, deletedOptionIds);
        }
    }

    /**
     * 상품 옵션 상세 삭제
     * 최소 1개의 옵션 상세 항목이 남아있어야 함
     */
    private void handleOptionDetailDeletions(Product product, UpdateProductRequest request) {
        List<Long> deletedDetailIds = request.getDeletedDetailIds();
        List<ProductOption> options = product.getOptions();

        if (deletedDetailIds != null && !deletedDetailIds.isEmpty()) {
            for (ProductOption option : options) {
                long remainingDetails = option.getDetails().stream()
                    .filter(detail -> !detail.getIsDeleted() && !deletedDetailIds.contains(detail.getId())) // 삭제할 세부 항목 제외
                    .count();
                if (remainingDetails <= 0) {
                    throw new ProductException(ErrorCode.AT_LEAST_ONE_OPTION_DETAIL_REQUIRED);
                }
            }
            adminProductDataProvider.deleteProductOptionDetails(options, deletedDetailIds);
        }
    }

}