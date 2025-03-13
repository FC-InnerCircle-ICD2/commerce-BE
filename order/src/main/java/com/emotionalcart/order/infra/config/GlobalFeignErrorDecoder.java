package com.emotionalcart.order.infra.config;

import com.emotionalcart.order.infra.advice.exceptions.*;
import com.emotionalcart.order.infra.product.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * feignClient global error handler
 */
@Slf4j
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
                log.error("Error responseBody : {}", responseBody);
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
                switch (errorResponse.getErrorCode()) {
                    case "PRODUCT-0007" -> throw new ProductPriceException(errorResponse.getErrorMessage());
                    case "PRODUCT-0009" -> throw new ProductValidationException(errorResponse.getErrorMessage());
                    case String s when s.startsWith("STOCK-") -> throw new StockException(errorResponse.getErrorMessage());
                    default -> throw new ProductStockException(errorResponse.getErrorMessage());

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
