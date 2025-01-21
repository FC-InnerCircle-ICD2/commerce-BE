package com.emotionalcart.order.infra.product;

import com.emotionalcart.order.infra.config.TestContainerConfiguration;
import com.emotionalcart.order.infra.utils.FileUtils;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.*;
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
    private ProductService productService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("상품 가격 조회 성공")
    void test_case_1() throws Exception {
        // given
        String request = FileUtils.readFileAsString("testcase/product/price/product_price_request_1.txt");
        String expect = FileUtils.readFileAsString("testcase/product/price/product_price_response_1.txt");

        // when
        ResponseDefinition response = stubFor(post(urlEqualTo("/api/v1/products/price"))
                                                  .withRequestBody(equalToJson(request))
                                                  .willReturn(aResponse()
                                                                  .withStatus(OK.value())
                                                                  .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                                                  .withBody(expect)
                                                  )).getResponse();
        // then
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("상품 가격 조회 실패 - 404")
    void test_case_2() throws Exception {
        // given
        String request = FileUtils.readFileAsString("testcase/product/price/product_price_request_1.txt");
        String expect = FileUtils.readFileAsString("testcase/product/price/product_price_fail_404.txt");
        // when
        stubFor(post(urlEqualTo("/api/v1/products/price"))
                    .withRequestBody(equalToJson(request))
                    .willReturn(aResponse()
                                    .withStatus(BAD_REQUEST.value())
                                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                    .withBody(expect)
                    )).getResponse();
        // then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductPrice(objectMapper.readValue(request, List.class)); // 예시
        });

        assertTrue(exception.getMessage().contains("PRODUCT-0007"));
        assertTrue(exception.getMessage().contains("해당 상품 옵션을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("상품 재고 변경 실패 - 409")
    void test_case_3() throws Exception {
        // given
        String request = FileUtils.readFileAsString("testcase/product/stock/product_stock_request_1.txt");
        String expect = FileUtils.readFileAsString("testcase/product/stock/product_stock_fail_409.txt");
        // when
        stubFor(post(urlEqualTo("/api/v1/products/stock"))
                    .withRequestBody(equalToJson(request))
                    .willReturn(aResponse()
                                    .withStatus(CONFLICT.value())
                                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                    .withBody(expect)
                    )).getResponse();
        // then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProductStock(objectMapper.readValue(request, List.class)); // 예시
        });

        assertTrue(exception.getMessage().contains("PRODUCT-0008"));
        assertTrue(exception.getMessage().contains("상품의 재고가 부족합니다."));
    }

    @Test
    @DisplayName("상품 유효성 검사 실패 - 400")
    void test_case_4() throws Exception {
        // given
        String request = FileUtils.readFileAsString("testcase/product/valid/product_valid_request_1.txt");
        String expect = FileUtils.readFileAsString("testcase/product/valid/product_valid_fail_400.txt");
        // when
        stubFor(post(urlEqualTo("/api/v1/products/validate"))
                    .withRequestBody(equalToJson(request))
                    .willReturn(aResponse()
                                    .withStatus(BAD_REQUEST.value())
                                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                                    .withBody(expect)
                    )).getResponse();
        // then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.isValidProduct(objectMapper.readValue(request, List.class)); // 예시
        });

        assertTrue(exception.getMessage().contains("PRODUCT-0009"));
        assertTrue(exception.getMessage().contains("상품의 필수 옵션이 선택되지 않았습니다."));

    }

}
