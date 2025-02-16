package com.emotionalcart.order.domain.entity;

import lombok.Getter;

@Getter
public class PriceAndQuantity {

    private double price;
    private double additionalPrice;
    private int quantity;

    public static PriceAndQuantity of(double price, double additionalPrice, int quantity) {
        PriceAndQuantity priceAndQuantity = new PriceAndQuantity();
        priceAndQuantity.price = price;
        priceAndQuantity.additionalPrice = additionalPrice;
        priceAndQuantity.quantity = quantity;
        return priceAndQuantity;
    }

}
