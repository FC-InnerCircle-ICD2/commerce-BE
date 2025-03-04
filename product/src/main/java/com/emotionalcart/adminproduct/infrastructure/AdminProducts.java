package com.emotionalcart.adminproduct.infrastructure;

import lombok.Data;

@Data
public class AdminProducts {

    private Long id;
    private String name;
    private Integer price;
    private String categoryName;
    private String providerName;
    private String mainImageUrl;

}
