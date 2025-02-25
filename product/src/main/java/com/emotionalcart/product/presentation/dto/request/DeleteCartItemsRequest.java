package com.emotionalcart.product.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DeleteCartItemsRequest {
    private List<CartItemToDelete> items;

    @Getter
    @Setter
    public static class CartItemToDelete {
        private Long productId;
        private Long optionId;
        private Long optionDetailId;
    }
}
