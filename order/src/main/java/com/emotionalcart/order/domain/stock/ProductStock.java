package com.emotionalcart.order.domain.stock;

import com.emotionalcart.order.presentation.client.dto.ProductStockResponse;

/**
 * 상품 재고 객체
 */
public class ProductStock {

    /**
     * 상품아이디
     */
    private Long productId;

    /**
     * 상품옵션아이디
     */
    private Long productOptionId;

    /**
     * 재고
     */
    private int quantity;

    public ProductStock(Long productId, Long productOptionId, int quantity) {
        this.productId = productId;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
    }

    public static ProductStock convert(ProductStockResponse productStockResponse) {
        return new ProductStock(productStockResponse.getProductId(),
                                productStockResponse.getProductOptionId(),
                                productStockResponse.getQuantity());
    }

}
