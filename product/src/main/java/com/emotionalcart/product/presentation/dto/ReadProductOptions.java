package com.emotionalcart.product.presentation.dto;

import java.util.List;

import com.emotionalcart.core.feature.product.ProductOption;

import lombok.Data;

public class ReadProductOptions {

    @Data
    public static class Response {
        private Long id;
        private String name;
        private List<ReadProductOptionDetails.Response> optionDetails;

        public Response(ProductOption productOption, List<ReadProductOptionDetails.Response> optionDetails) {
            this.id = productOption.getId();
            this.name = productOption.getName();
            this.optionDetails = optionDetails;
        }

        public static Response toResponse(ProductOption productOption,
                List<ReadProductOptionDetails.Response> optionDetails) {
            return new Response(productOption, optionDetails);
        }
    }

}
