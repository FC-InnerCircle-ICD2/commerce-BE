package com.emotionalcart.order.presentation.controller;

import com.emotionalcart.order.infra.dto.BestSellingProduct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "주문", description = "주문 통계 API")
public interface OrderStatisticsDocs {

    @Operation(summary = "카테고리 별 상품 판매량 많은 순 조회 API")
    ResponseEntity<Page<BestSellingProduct>> getProductRanking(@PathVariable Long categoryId, Pageable request);

}
