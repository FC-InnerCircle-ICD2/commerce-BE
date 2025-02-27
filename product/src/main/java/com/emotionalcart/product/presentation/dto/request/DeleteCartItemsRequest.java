package com.emotionalcart.product.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DeleteCartItemsRequest {
    private List<CartItemToDelete> items;

    @Getter
    @Setter
    public static class CartItemToDelete {
        private Long productId;
        // 상품 옵션 및 상품 상세 옵션 맵 배열
        private List<Map<Long, Long>> optionDetailIdMapList;
    }
}
