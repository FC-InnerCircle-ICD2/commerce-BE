package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.application.OrderStatisticsService;
import com.emotionalcart.order.infra.dto.BestSellingProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderStatisticsController implements OrderStatisticsDocs {

    private final OrderStatisticsService orderStatisticsService;

    /**
     * 카테고리 별 판매량 많은 상품 조회
     * x
     *
     * @param categoryId 카테고리 식별자
     * @param request    페이징
     * @return
     */
    @GetMapping("/sales-ranking/{categoryId}")
    public ResponseEntity<Page<BestSellingProduct>> getProductRanking(@PathVariable Long categoryId,
                                                                      @PageableDefault(sort = "totalQuantitySold", direction = Sort.Direction.DESC) Pageable request) {
        return ResponseEntity.ok().body(orderStatisticsService.getProductRankingByCategoryId(categoryId,
                                                                                             request));
    }

}
