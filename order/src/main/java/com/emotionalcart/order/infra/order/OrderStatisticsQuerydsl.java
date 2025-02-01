package com.emotionalcart.order.infra.order;

import com.emotionalcart.order.domain.dto.BestSellingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderStatisticsQuerydsl {

    Page<BestSellingProduct> getProductRankingsByCategoryId(Long categoryId, Pageable page);

}
