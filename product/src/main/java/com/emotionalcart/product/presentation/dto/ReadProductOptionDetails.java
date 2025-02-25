package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.product.ProductOptionDetail;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

public class ReadProductOptionDetails {

    @Data
    public static class Response {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String value;
        private Integer order;
        private Integer additionalPrice;

        private Response(ProductOptionDetail optionDetail) {
            this.id = optionDetail.getId();
            this.value = optionDetail.getValue();
            this.order = optionDetail.getOptionOrder();
            this.additionalPrice = optionDetail.getAdditionalPrice();
        }

        public static Response toResponse(ProductOptionDetail optionDetail) {
            return new Response(optionDetail);
        }
    }
}
