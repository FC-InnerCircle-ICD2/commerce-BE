package com.emotionalcart.product.presentation.dto.support;

import lombok.Data;

public class Carts {

    @Data
    public static class Option {
        private Long id;
        private String name;
        private OptionDetail optionDetail;
    }

    @Data
    public static class OptionDetail {
        private Long id;
        private String value;
        private int quantity;
        private int additionalPrice;
    }

    @Data
    public static class Image {
        private Long id;
        private String url;
    }

    @Data
    public static class Provider {
        private Long id;
        private String name;
    }
}
