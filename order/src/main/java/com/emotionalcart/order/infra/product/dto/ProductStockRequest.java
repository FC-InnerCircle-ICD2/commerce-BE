package com.emotionalcart.order.infra.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 재고 요청
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockRequest {

    private Long productId;

    private List<ProductOption> productOptions;

    public static ProductStockRequest of(Long productId) {
        ProductStockRequest request = new ProductStockRequest();
        request.productId = productId;
        return request;
    }

    public void addOption(Long productOptionId, Long productOptionDetailId, int quantity) {
        if (CollectionUtils.isEmpty(this.productOptions)) {
            this.productOptions = new ArrayList<>();
        }
        this.productOptions.add(ProductOption.of(productOptionId, productOptionDetailId, quantity));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductOption {

        /**
         * 상품 옵션 아이디
         */
        private Long productOptionId;

        /**
         * 상품 옵션 상세 아이디
         */
        private Long productOptionDetailId;

        /**
         * 수량
         */
        private int quantity;

        public static ProductOption of(Long productOptionId, Long productOptionDetailId, int quantity) {
            ProductOption productOption = new ProductOption();
            productOption.productOptionId = productOptionId;
            productOption.productOptionDetailId = productOptionDetailId;
            productOption.quantity = quantity;
            return productOption;
        }

    }

}
