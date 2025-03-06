package com.emotionalcart.order.infra.search.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;

@Getter
public class ProductAdminResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    private String highlight;

}
