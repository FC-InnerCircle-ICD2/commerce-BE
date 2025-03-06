package com.emotionalcart.adminproduct.application.event;

import com.emotionalcart.adminproduct.domain.event.ProductCreatedEvent;
import com.emotionalcart.adminproduct.infrastructure.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminProductEventListener {
    private SearchService searchService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("ProductCreatedEvent: {}", event);
        searchService.indexCreateProduct(event);
    }
}
