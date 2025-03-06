package com.emotionalcart.order.infra.search.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;

@Getter
public class OrderSearchResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    private String highlight;

}
