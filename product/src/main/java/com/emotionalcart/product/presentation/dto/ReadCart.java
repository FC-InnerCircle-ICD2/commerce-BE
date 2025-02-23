package com.emotionalcart.product.presentation.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import com.emotionalcart.product.presentation.dto.request.AddCartItemRequest;

public class ReadCart {

    @Data
    public static class Response implements Serializable {
        private static final long serialVersionUID = 1L;
        private String cartId;
        private int totalQuantity;
        private int totalPrice;
        private List<CartItem> items = new ArrayList<>(); // 장바구니 항목 리스트.
    }

    @Data
    public static class CartItem implements Serializable {
        private static final long serialVersionUID = 1L;
        // 상품 정보
        private Long productId;
        private String productName;
        private int productPrice;

        // 상품 옵션 정보
        private Long optionId;
        private String optionName;

        // 상품 상세 옵션
        private Long detailOptionId;
        private String detailOptionValue;
        private int detailOptionQuantity;

        // 상품 대표 이미지
        private Long imageId;
        private String imageUrl;

        // 카테고리
        private Long categoryId;
        private String categoryName;

        // 공급자
        private Long providerId;
        private String providerName;

        public static CartItem from(AddCartItemRequest request) {
            CartItem cartItem = new CartItem();
            cartItem.productId = request.getProductId();
            cartItem.productName = request.getProductName();
            cartItem.productPrice = request.getProductPrice();
            cartItem.optionId = request.getOptionId();
            cartItem.optionName = request.getOptionName();
            cartItem.detailOptionId = request.getDetailOptionId();
            cartItem.detailOptionValue = request.getDetailOptionValue();
            cartItem.detailOptionQuantity = request.getDetailOptionQuantity();
            cartItem.imageId = request.getImageId();
            cartItem.imageUrl = request.getImageUrl();
            cartItem.categoryId = request.getCategoryId();
            cartItem.categoryName = request.getCategoryName();
            cartItem.providerId = request.getProviderId();
            cartItem.providerName = request.getProviderName();
            return cartItem;
        }
    }
}
