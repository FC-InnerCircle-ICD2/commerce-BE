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
        private int totalPrice;
        private List<CartItem> items = new ArrayList<>(); // 장바구니 항목 리스트.

        public int calculateTotal() {
            return items.stream()
                    .filter(CartItem::isSelected)
                    .mapToInt(item -> item.getSubTotalPrice())
                    .sum();
        }
    }

    @Data
    public static class CartItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long productId;
        private String productName;
        private int price;
        private int subTotalPrice;
        private Option option;
        private Image images;
        private Provider provider;
        private boolean isSelected;

        public void setOptionDetailQuantity(int quantity) {
            if (this.option != null && this.option.getOptionDetail() != null) {
                this.option.getOptionDetail().setQuantity(quantity);
            }
        }

        public int getOptionDetailQuantity() {
            if (this.option != null && this.option.getOptionDetail() != null) {
                return this.option.getOptionDetail().getQuantity();
            }
            return 0; // or throw an exception if appropriate
        }

        public static CartItem from(AddCartItemRequest request) {
            CartItem cartItem = new CartItem();
            cartItem.productId = request.getProductId();
            cartItem.productName = request.getProductName();
            cartItem.price = request.getPrice();
            cartItem.subTotalPrice = request.getSubTotalPrice();
            Option option = new Option();
            option.id = request.getOptionId();
            option.name = request.getOptionName();
            OptionDetail optionDetail = new OptionDetail();
            optionDetail.id = request.getOptionDetailId();
            optionDetail.value = request.getOptionDetailValue();
            optionDetail.quantity = request.getOptionDetailQuantity();
            optionDetail.additionalPrice = request.getOptionDetailAdditionalPrice();
            option.optionDetail = optionDetail;
            cartItem.option = option;
            Image image = new Image();
            image.id = request.getImageId();
            image.url = request.getImageUrl();
            cartItem.images = image;
            Provider provider = new Provider();
            provider.id = request.getProviderId();
            provider.name = request.getProviderName();
            cartItem.provider = provider;
            return cartItem;
        }
    }

    @Data
    public static class Option {
        private Long id;
        private String name;
        private OptionDetail optionDetail;
    }

    @Data
    public static class OptionDetail {
        private Long id;
        private String value;
        private int quantity;
        private int additionalPrice;
    }

    @Data
    public static class Image {
        private Long id;
        private String url;
    }

    @Data
    public static class Provider {
        private Long id;
        private String name;
    }
}
