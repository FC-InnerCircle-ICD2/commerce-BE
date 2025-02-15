package com.emotionalcart.adminproduct.presentation.dto;

import com.emotionalcart.core.feature.product.Product;
import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CreateProductRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Integer price;
    @NotNull
    private Long providerId;
    @NotNull
    private Long categoryId;
    @NotNull
    private List<CreateProductOption> options;
    @NotNull
    private MultipartFile mainImage;
    @NotNull
    private List<MultipartFile> detailImages;

    @Getter
    @Setter
    static class CreateProductOption {
        private String name;
        private List<CreateProductOptionDetail> optionDetails;

        public ProductOption toEntity(Product product) {
            ProductOption productOption = ProductOption.of(name);
            productOption.setProduct(product);

            // 옵션 상세 추가
            if (optionDetails != null) {
                List<ProductOptionDetail> details = optionDetails.stream()
                        .map(option -> option.toEntity(productOption))
                        .collect(Collectors.toList());
                productOption.setDetails(details);
            }

            return productOption;
        }
    }


    @Getter
    @Setter
    static class CreateProductOptionDetail {
        @NotNull
        private String value;
        @NotNull
        private Integer optionOrder;
        private Integer additionalPrice = 0;

        public ProductOptionDetail toEntity(ProductOption productOption) {
            return ProductOptionDetail.of(
                    value,
                    optionOrder,
                    additionalPrice,
                    productOption
            );
        }
    }

    public Product toEntity() {
        Product product = Product.of(
                name,
                description,
                price,
                providerId,
                categoryId
        );

        // 옵션 추가
        if (options != null) {
            List<ProductOption> productOptions = options.stream()
                    .map(option -> option.toEntity(product))
                    .collect(Collectors.toList());
            product.setOptions(productOptions);
        }

        return product;
    }
}


