package com.emotionalcart.adminproduct.application.event;

import com.emotionalcart.adminproduct.domain.event.ProductCreatedEvent;
import com.emotionalcart.adminproduct.domain.event.ProductDeletedEvent;
import com.emotionalcart.adminproduct.domain.event.ProductStockCreateEvent;
import com.emotionalcart.adminproduct.infrastructure.search.SearchService;
import com.emotionalcart.product.infrastructure.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminProductEventListener {

    private final SearchService searchService;
    private final StockService stockService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("ProductCreatedEvent: {}", event);
        searchService.indexCreateProduct(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductDeleted(ProductDeletedEvent event) {
        log.info("ProductDeletedEvent: {}", event);
        searchService.indexDeleteProduct(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductStockCreate(ProductStockCreateEvent event) {
        log.info("ProductStockCreateEvent: {}", event);
        stockService.generateOptionCombinationsAndSaveStock(event.id());
    }
}
