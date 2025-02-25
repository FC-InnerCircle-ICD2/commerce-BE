package com.emotionalcart.adminproduct.presentation.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadAdminProductsResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private Integer price;
    private String categoryName;
    private String providerName;
    private String mainImageUrl;

}
