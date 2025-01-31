package com.emotionalcart.order.application.service;

import com.emotionalcart.order.domain.dto.BestSellingProduct;
import com.emotionalcart.order.infra.order.OrderStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

    private final OrderStatisticsRepository orderStatisticsRepository;

    /**
     * 카테고리 별로 상품 순위 조회
     *
     * @param categoryId 자식 카테고리 식별자
     * @param request    페이징
     * @return
     */
    public List<BestSellingProduct> getProductRankingByCategoryId(Long categoryId, PageRequest request) {
        return orderStatisticsRepository.getProductRankingsByCategoryId(categoryId, request);
    }

}
