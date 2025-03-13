package com.emotionalcart.product.presentation.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import com.emotionalcart.product.presentation.dto.request.AddCartItemRequest;
import com.emotionalcart.product.presentation.dto.support.Carts;

public class ReadCart {

    @Data
    public static class Response implements Serializable {
        @Serial
        private static final long serialVersionUID = 7563541798002866698L;
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
        @Serial
        private static final long serialVersionUID = -5811050683632008129L;
        private String itemId;
        private String productId;
        private String productName;
        private int price;
        private int subTotalPrice;
        private List<Carts.Option> options = new ArrayList<>();
        private Carts.Image images;
        private Carts.Provider provider;
        private boolean isSelected;
        private int itemOrder;

        public static CartItem from(AddCartItemRequest request, String itemId, int itemOrder, int subTotalPrice) {
            CartItem cartItem = new CartItem();
            cartItem.itemId = itemId;
            cartItem.itemOrder = itemOrder;
            cartItem.productId = String.valueOf(request.getProductId());
            cartItem.productName = request.getProductName();
            cartItem.price = request.getPrice();
            cartItem.subTotalPrice = subTotalPrice;

            List<Carts.Option> readCartOptions = new ArrayList<>();
            for (Carts.Option reqOption : request.getOptions()) {
                Carts.Option option = new Carts.Option();
                option.setId(reqOption.getId());
                option.setName(reqOption.getName());

                Carts.OptionDetail optionDetail = new Carts.OptionDetail();
                optionDetail.setId(reqOption.getOptionDetail().getId());
                optionDetail.setValue(reqOption.getOptionDetail().getValue());
                optionDetail.setQuantity(reqOption.getOptionDetail().getQuantity());
                optionDetail.setAdditionalPrice(reqOption.getOptionDetail().getAdditionalPrice());

                option.setOptionDetail(optionDetail);
                readCartOptions.add(option);
            }
            cartItem.options = readCartOptions;

            Carts.Image image = new Carts.Image();
            image.setId(request.getImages().getId());
            image.setUrl(request.getImages().getUrl());
            cartItem.images = image;

            Carts.Provider provider = new Carts.Provider();
            provider.setId(request.getProvider().getId());
            provider.setName(request.getProvider().getName());
            cartItem.provider = provider;

            cartItem.isSelected = true;
            return cartItem;
        }
    }
}
