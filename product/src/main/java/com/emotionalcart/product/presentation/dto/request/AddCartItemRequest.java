package com.emotionalcart.product.presentation.dto.request;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.emotionalcart.product.presentation.dto.support.Carts;

@Getter
@Setter
public class AddCartItemRequest {

    private Long productId;
    private String productName;
    private int price;
    private List<Carts.Option> options;
    private Carts.Image images;
    private Carts.Provider provider;

}
