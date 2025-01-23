package com.emotionalcart.product.application;

import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductImage;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.review.Review;
import com.emotionalcart.product.domain.dto.ProductOptionDetailWithImages;
import com.emotionalcart.product.domain.support.ProductOptionDetails;
import com.emotionalcart.product.domain.support.ProductOptions;
import com.emotionalcart.product.domain.support.Products;
import com.emotionalcart.product.presentation.dto.ReadCategories;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.domain.ProductDataProvider;
import com.emotionalcart.core.feature.category.Category;
//import com.emotionalcart.product.presentation.dto.ReadProductDetail;
//import com.emotionalcart.product.domain.Product;
//import com.emotionalcart.product.domain.repository.ProductRepository;
import com.emotionalcart.product.presentation.dto.ReadProducts;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDataProvider productDataProvider;

    public List<ReadCategories.Response> getAllProductCategories() {
        List<Category> categories = productDataProvider.findAllCategories();

        return ReadCategories.Response.toResponse(categories);
    }

    public Page<ReadProductReviews.Response> readProductReviews(@NotNull Long productId,
            ReadProductReviews.Request request) {
        productDataProvider.findProduct(productId);

        Page<Review> reviews = productDataProvider.findAllReviews(productId, request.getPageable());
        ReviewImages reviewImages = findAllReviewImages(reviews.getContent());

        return ReadProductReviews.Response.toResponse(reviews, reviewImages);
    }

    private ReviewImages findAllReviewImages(List<Review> reviews) {
        Reviews from = Reviews.from(reviews);
        return ReviewImages.from(productDataProvider.findAllReviewImages(from.ids()));
    }

//    public ReadProductDetail.Response getProductDetail(Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//        return new ReadProductDetail.Response(product);
//    }

    public Page<ReadProducts.Response> readProducts(ReadProducts.Request request) {
        Page<Product> products = productDataProvider.findAllProducts(request, request.getPageable());

        ProductOptions productOptions = findProductOptions(products.getContent());

        ProductOptionDetails optionDetails = findProductOptionDetails(productOptions.ids());

        Map<Long, Double> ratings = findProductRatings(products.getContent());

        return ReadProducts.Response.toResponse(products, productOptions, optionDetails, ratings);
    }

    private ProductOptions findProductOptions(List<Product> products) {
        Products from = Products.from(products);
        List<ProductOption> productOptions = productDataProvider.findProductOptions(from.ids());
        return ProductOptions.from(productOptions);
    }

    private ProductOptionDetails findProductOptionDetails(Set<Long> ids) {
        List<ProductOptionDetailWithImages> optionWithDetails = productDataProvider.findProductOptionDetails(ids);
        return ProductOptionDetails.from(optionWithDetails);
    }

    private Map<Long, Double> findProductRatings(List<Product> products){
        return productDataProvider.findProductRatings(products.stream()
                .map(Product::getId)
                .toList());
    }
}
