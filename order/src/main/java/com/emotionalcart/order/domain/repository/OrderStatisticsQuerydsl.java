package com.emotionalcart.order.domain.repository;

import com.emotionalcart.order.infra.dto.BestSellingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderStatisticsQuerydsl {

    Page<BestSellingProduct> getProductRankingsByCategoryId(Long categoryId, Pageable page);

}
