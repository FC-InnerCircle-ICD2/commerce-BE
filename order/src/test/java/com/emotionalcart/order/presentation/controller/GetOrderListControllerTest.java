package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.infra.config.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
class GetOrderListControllerTest {

    @Test
    @DisplayName("주문 목록 조회 테스트")
    void test_case_1() throws Exception {
        // given
        
        // when

        // then

    }

}
