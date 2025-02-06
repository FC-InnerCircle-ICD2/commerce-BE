package com.emotionalcart.order.infra.config;

import com.emotionalcart.order.infra.advice.exceptions.FeignClientDecodingException;
import com.emotionalcart.order.infra.advice.exceptions.ProductPriceException;
import com.emotionalcart.order.infra.advice.exceptions.ProductStockException;
import com.emotionalcart.order.infra.advice.exceptions.ProductValidationException;
import com.emotionalcart.order.infra.product.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * feignClient global error handler
 */
public class GlobalFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());

        if (!httpStatus.is2xxSuccessful()) {
            try {
                // Response body를 문자열로 변환
                String responseBody = responseBodyToString(response);
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

                switch (errorResponse.getErrorCode()) {
                    case "PRODUCT-0007" -> throw new ProductPriceException(errorResponse.toString());
                    case "PRODUCT-0009" -> throw new ProductValidationException(errorResponse.toString());
                    default -> throw new ProductStockException(errorResponse.toString());

                }
            } catch (IOException e) {
                throw new FeignClientDecodingException(e.getMessage());
            }
        }

        return errorDecoder.decode(methodKey, response);
    }

    /**
     * Response body를 문자열로 변환하는 메서드
     */
    private String responseBodyToString(Response response) throws IOException {
        if (response.body() == null) {
            return "";
        }

        try (var reader = response.body().asInputStream()) {
            return new String(reader.readAllBytes());
        }
    }

}
