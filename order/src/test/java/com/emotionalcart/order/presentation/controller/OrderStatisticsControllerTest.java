package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.application.OrderStatisticsService;
import com.emotionalcart.order.domain.dto.BestSellingProduct;
import com.emotionalcart.order.presentation.controller.response.SalesRankingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderStatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderStatisticsService orderStatisticsService;

    @InjectMocks
    private OrderStatisticsController orderStatisticsController;

    @BeforeEach
    void setUp() {
        // MockMvc를 컨트롤러와 함께 수동으로 설정
        mockMvc =
            MockMvcBuilders.standaloneSetup(orderStatisticsController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @Test
    @DisplayName("카테고리별 판매량 많은 상품 조회 - 성공")
    void getProductRanking_Success() throws Exception {
        // Given (Mock 데이터 생성)
        List<BestSellingProduct> productList = List.of(
            new BestSellingProduct(1L, 100L),
            new BestSellingProduct(2L, 80L)
        );

        Page<BestSellingProduct> productPage = new PageImpl<>(productList, PageRequest.of(0, 10), productList.size());
        SalesRankingResponse response = SalesRankingResponse.from(productPage);

        // orderStatisticsService Mock 설정
        Mockito.when(orderStatisticsService.getProductRankingByCategoryId(eq(1L), any()))
            .thenReturn(productPage);

        // When & Then (API 호출 및 검증)
        mockMvc.perform(get("/api/v1/orders/sales-ranking/1").param("page", "1").param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // HTTP 200 응답 확인
            .andExpect(jsonPath("$.productList[0].productId").value(1L)) // 첫 번째 상품 ID 확인
            .andExpect(jsonPath("$.productList[0].salesCount").value(100L)) // 첫 번째 판매량 확인
            .andExpect(jsonPath("$.productList[1].productId").value(2L)) // 두 번째 상품 ID 확인
            .andExpect(jsonPath("$.productList[1].salesCount").value(80L)) // 두 번째 판매량 확인
            .andExpect(jsonPath("$.totalPages").value(1)) // 전체 페이지 수 확인
            .andExpect(jsonPath("$.totalElements").value(2)) // 전체 요소 개수 확인
            .andExpect(jsonPath("$.currentPage").value(0)) // 현재 페이지 번호 확인
            .andExpect(jsonPath("$.size").value(10)); // 페이지 크기 확인
    }

}