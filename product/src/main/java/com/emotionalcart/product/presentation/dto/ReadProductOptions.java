package com.emotionalcart.product.presentation.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.emotionalcart.core.feature.product.ProductOption;
import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.core.feature.product.ProductImage;

import lombok.Data;

public class ReadProductOptions {

    @Data
    public static class Response {
        private Long id;
        private String name;
        private List<ReadProductOptionDetails.Response> productOptionDetails;

        public Response(ProductOption productOption, List<ReadProductOptionDetails.Response> productOptionDetails) {
            this.id = productOption.getId();
            this.name = productOption.getName();
            this.productOptionDetails = productOptionDetails;
        }

        public static Response toResponse(ProductOption productOption, List<ProductOptionDetail> productOptionDetails,
                List<ProductImage> productImages) {
            List<ReadProductOptionDetails.Response> readProductOptionDetails = productOptionDetails.stream()
                    .map(detail -> ReadProductOptionDetails.Response.toResponse(detail, productImages))
                    .collect(Collectors.toList());

            return new Response(productOption, readProductOptionDetails);
        }
    }

}
