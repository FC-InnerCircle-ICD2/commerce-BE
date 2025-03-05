package com.emotionalcart.product.presentation.dto.support;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

public class Carts {

    @Data
    public static class Option implements Serializable {
        @Serial
        private static final long serialVersionUID = 5170093473466125631L;
        //        private static final long serialVersionUID = 1L;
        private Long id;
        private String name;
        private OptionDetail optionDetail;
    }

    @Data
    public static class OptionDetail implements Serializable {
        @Serial
        private static final long serialVersionUID = 5877740958849796845L;
        //        private static final long serialVersionUID = 1L;
        private Long id;
        private String value;
        private int quantity;
        private int additionalPrice;
    }

    @Data
    public static class Image implements Serializable {
        @Serial
        private static final long serialVersionUID = 7187402760291111972L;
        //        private static final long serialVersionUID = 1L;
        private Long id;
        private String url;
    }

    @Data
    public static class Provider implements Serializable {
        @Serial
        private static final long serialVersionUID = -1260464229945702192L;
        //        private static final long serialVersionUID = 1L;
        private Long id;
        private String name;
    }
}
