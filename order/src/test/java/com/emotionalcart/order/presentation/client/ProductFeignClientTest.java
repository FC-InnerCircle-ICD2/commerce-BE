package com.emotionalcart.order.presentation.client;

import com.emotionalcart.order.domain.stock.service.ProductStockService;
import com.emotionalcart.order.infrastructure.configuration.TestContainerConfiguration;
import com.emotionalcart.order.presentation.client.dto.ProductStockRequest;
import com.emotionalcart.order.presentation.client.dto.UpdateStockRequest;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@Import(TestContainerConfiguration.class)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
    "product.find.feign-endpoint=http://localhost:${wiremock.server.port}"
})
class ProductFeignClientTest {

    @Autowired
    private ProductStockService productStockService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("상품 재고 조회 성공")
    void success() {
        // given: 요청 데이터 정의
        String requestBody = """
            {
                "products": [
                    {
                        "productId": 1,
                        "productOptionId": 1
                    }
                ]
            }
            """;

        String expect = """
            {
                "data": [
                    {
                        "productId": 1,
                        "productOptionId": 1,
                        "stock": 10
                    }
                ]
            }
            """;

        // then: 응답 검증
        ResponseDefinition response = stubFor(post(urlEqualTo("/api/v1/product/stock"))
                                                  .withRequestBody(equalToJson(requestBody))
                                                  .willReturn(aResponse()
                                                                  .withStatus(OK.value())
                                                                  .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                                                  .withBody(expect))).getResponse();
        // then
        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("상품 재고 조회 실패 - 500 Internal Server Error")
    void failure_internalServerError() {
        // given: 요청 데이터 정의
        String requestBody = """
            {
                "products": [
                    {
                        "productId": 1,
                        "productOptionId": 1
                    }
                ]
            }
            """;

        String errorResponse = """
            {
                "errorCode": "ORDER-103",
                "errorMessage": "상품 재고 조회를 실패하였습니다."
            }
            """;

        // when: WireMock에 실패 응답 정의
        stubFor(post(urlEqualTo("/api/v1/product/stock"))
                    .withRequestBody(equalToJson(requestBody))
                    .willReturn(aResponse()
                                    .withStatus(500)
                                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                    .withBody(errorResponse)));

        // then: 응답 검증
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productStockService.getStock(objectMapper.readValue(requestBody, ProductStockRequest.class)); // 예시
        });

        assertTrue(exception.getMessage().contains("상품 재고 조회를 실패하였습니다."));
    }

    @Test
    @DisplayName("재고 변경 성공")
    void product_stock_update_success() {
        // given
        String requestBody = """
            {
                "products": [
                    {
                        "productId": 1,
                        "productOptionId": 1,
                        "quantity":1
                    }
                ]
            }
            """;
        // when
        // then: 응답 검증
        ResponseDefinition response = stubFor(post(urlEqualTo("/api/v1/product/stock"))
                                                  .withRequestBody(equalToJson(requestBody))
                                                  .willReturn(aResponse()
                                                                  .withStatus(OK.value())
                                                                  .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                                  )).getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("재고 변경 실패")
    void product_stock_update_fail() {
        // given
        String requestBody = """
            {
                "products": [
                    {
                        "productId": 1,
                        "productOptionId": 1,
                        "quantity":1
                    }
                ]
            }
            """;
        // when
        String expect = """
            {
                "errorCode": "PRODUCT-001",
                "errorMessage": "상품 재고 변경에 실패하였습니다."
            }
            """;

        // then: 응답 검증
        stubFor(put(urlEqualTo("/api/v1/product/stock"))
                    .withRequestBody(equalToJson(requestBody))
                    .willReturn(aResponse()
                                    .withStatus(500)
                                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                    .withBody(expect)));

        // then: 응답 검증
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productStockService.updateStock(objectMapper.readValue(requestBody, UpdateStockRequest.class)); // 예시
        });

        assertTrue(exception.getMessage().contains("상품 재고 변경에 실패하였습니다."));
    }

}