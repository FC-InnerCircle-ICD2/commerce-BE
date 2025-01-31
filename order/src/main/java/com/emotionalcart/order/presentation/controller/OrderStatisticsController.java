package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.application.service.OrderStatisticsService;
import com.emotionalcart.order.presentation.controller.response.SalesRankingResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "order", description = "order statistics API")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderStatisticsController {

    private final OrderStatisticsService orderStatisticsService;

    /**
     * 카테고리 별 판매량 많은 상품 조회
     *
     * @param categoryId 카테고리 식별자
     * @param request    페이징
     * @return
     */
    @GetMapping("/sales-ranking/{categoryId}")
    public ResponseEntity<SalesRankingResponse> getProductRanking(@PathVariable Long categoryId, PageRequest request) {
        return ResponseEntity.ok().body(SalesRankingResponse.from(orderStatisticsService.getProductRankingByCategoryId(categoryId,
                                                                                                                       request)));
    }

}
