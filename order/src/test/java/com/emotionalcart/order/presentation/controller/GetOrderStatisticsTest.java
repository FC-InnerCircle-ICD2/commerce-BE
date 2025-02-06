package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.infra.utils.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class GetOrderStatisticsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = "/databases/insert_order_statistics.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("데이터 삽입 후 통계 API 테스트")
    void test_case_1() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = FileUtils.readFileAsString("testcase/order_statistics/get_order_statistics.txt");
        JsonNode expectedJsonNode = objectMapper.readTree(expectedJson);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/orders/sales-ranking/{categoryId}", 501)
                                                  .param("page", "0")
                                                  .param("size", "10")
                                                  .param("sort", "totalQuantitySold,desc")
                                                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        // then: 응답 JSON을 객체로 변환 후 비교
        String actualJson = mvcResult.getResponse().getContentAsString();
        JsonNode actualJsonNode = objectMapper.readTree(actualJson);

        assertEquals(expectedJsonNode, actualJsonNode);
    }

}
