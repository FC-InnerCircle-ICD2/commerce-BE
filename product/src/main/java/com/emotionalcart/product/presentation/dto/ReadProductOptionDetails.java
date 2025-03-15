package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

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

        public Response(Long id, @NotNull String value, @NotNull Integer optionOrder, Integer additionalPrice) {
            this.id = id;
            this.value = value;
            this.order = optionOrder;
            this.additionalPrice = additionalPrice;
        }

        public static Response toResponse(ProductOptionDetail optionDetail) {
            return new Response(optionDetail);
        }

        public static Response from(ProductOptionDetail detail) {
            return new Response(detail.getId(), detail.getValue(), detail.getOptionOrder(), detail.getAdditionalPrice());
        }

        public static List<Response> from(List<ProductOptionDetail> details) {
            return details.stream().map(Response::from).toList();
        }

    }

}
