package com.emotionalcart.product.infrastructure.stock.dto;

import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OptionStockDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;

    public OptionStockDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<OptionStockDto> toResponse(List<ProductOptionDetail> productOptionDetails){
        return productOptionDetails.stream()
                .map(detail -> new OptionStockDto(detail.getId(), detail.getValue()))
                .toList();
    }
}
