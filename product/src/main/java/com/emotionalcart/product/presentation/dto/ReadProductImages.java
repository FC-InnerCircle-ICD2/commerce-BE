package com.emotionalcart.product.presentation.dto;

import lombok.Data;

public class ReadProductImages {

    @Data
    public static class Response {
        private Long id;
        private String url;
    }

}
