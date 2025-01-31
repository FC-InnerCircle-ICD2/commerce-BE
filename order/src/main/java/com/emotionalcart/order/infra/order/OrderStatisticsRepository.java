package com.emotionalcart.order.infra.order;

import com.emotionalcart.order.domain.entity.OrderStatistics;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatisticsRepository extends JpaRepository<OrderStatistics, Long>, OrderStatisticsQuerydsl {

    Optional<OrderStatistics> findByProductIdAndCategoryId(@NotNull(message = "상품을 선택해주세요.") Long productId,
                                                           @NotNull(message = "상품 카테고리를 확인해주세요.") Long categoryId);

}