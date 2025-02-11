package com.emotionalcart.product.domain.event;

import com.emotionalcart.core.exception.ErrorCode;
import com.emotionalcart.core.exception.ProductException;
import com.emotionalcart.core.feature.review.ReviewStatistic;
import com.emotionalcart.product.infrastructure.repository.ReviewStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReviewEventListener {
    private final ReviewStatisticRepository reviewStatisticRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateReviewStatistics(UpdateReviewStatisticEvent event) {
        // 상품별 리뷰 수 & 평점 업데이트
        ReviewStatistic reviewStatistic = reviewStatisticRepository.findByProductId(event.getProductId())
                .orElseThrow(() -> new ProductException(ErrorCode.NOT_FOUND_REVIEW_STATISTIC));

        reviewStatistic.updateStatistics(event.getRating());
    }
}
