package com.emotionalcart.order.infra.order;

import com.emotionalcart.order.domain.dto.BestSellingProduct;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderStatisticsQuerydsl {

    List<BestSellingProduct> getProductRankingsByCategoryId(Long categoryId, PageRequest page);

}
