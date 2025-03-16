package com.emotionalcart.product.presentation.dto;

import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

public class ReadProductOptions {

    @Data
    public static class Response {

        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;
        private String name;
        private List<ReadProductOptionDetails.Response> optionDetails;

        public Response(ProductOption productOption, List<ReadProductOptionDetails.Response> optionDetails) {
            this.id = productOption.getId();
            this.name = productOption.getName();
            this.optionDetails = optionDetails;
        }

        public Response(Long id, @NotNull String name, List<ProductOptionDetail> details) {
            this.id = id;
            this.name = name;
            this.optionDetails = ReadProductOptionDetails.Response.from(details);
        }

        public static Response toResponse(ProductOption productOption,
                                          List<ReadProductOptionDetails.Response> optionDetails) {
            return new Response(productOption, optionDetails);
        }

        public static Response from(ProductOption option) {
            return new Response(option.getId(), option.getName(), option.getDetails());
        }

        public static List<Response> from(List<ProductOption> options) {
            return options.stream().map(Response::from).toList();
        }

    }

}
