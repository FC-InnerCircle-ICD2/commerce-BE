package com.emotionalcart.product.presentation.dto.request;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.emotionalcart.product.presentation.dto.support.Carts;

@Getter
@Setter
public class AddCartItemRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -2922855908274416841L;

    private Long productId;
    private String productName;
    private int price;
    private List<Carts.Option> options;
    private Carts.Image images;
    private Carts.Provider provider;

}
