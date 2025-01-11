package com.emotionalcart.order.domain.entity;

import lombok.Getter;

@Getter
public class PriceAndQuantity {

    private double price;
    private int quantity;

    public static PriceAndQuantity of(double price, int quantity) {
        PriceAndQuantity priceAndQuantity = new PriceAndQuantity();
        priceAndQuantity.price = price;
        priceAndQuantity.quantity = quantity;
        return priceAndQuantity;
    }

}
