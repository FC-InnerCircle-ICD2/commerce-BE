package com.emotionalcart.stock.presentation;

import com.emotionalcart.stock.application.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class StockController {

    private final StockQueryService stockQueryService;
    private final StockCommandService stockCommandService;

    /**
     * <pre>
     * 재고 조회 API
     * </pre>
     */
    @PostMapping("/quantity")
    public ResponseEntity<Integer> getStockQuantity(@Valid @RequestBody StockQuantitySearchRequest request) {
        return ResponseEntity.ok(stockQueryService.getStockQuantityByOptionIds(request.mapToQuery()));
    }

    @PostMapping("/quantities")
    public ResponseEntity<List<StockQuantity>> getStockQuantities(@Valid @RequestBody List<StockQuantitySearchRequest> requests) {
        return ResponseEntity.ok(stockQueryService.getStockQuantitiesByOptionIds(requests.stream().map(StockQuantitySearchRequest::mapToQuery).toList()));
    }

    @PostMapping("/quantity/validate")
    public ResponseEntity<Boolean> getStockQuantities(@Valid @RequestBody StockQuantityValidateRequest requests) {
        return ResponseEntity.ok(stockCommandService.validateStockQuantity(requests.mapToCommand()));
    }

    /**
     * <pre>
     * 옵션 조합 생성 및 재고 저장 API
     * </pre>
     */
    @PostMapping("/product-id/{productId}")
    public ResponseEntity<List<CreatedStock>> generateOptionCombinationsAndSaveStock(@PathVariable(value = "productId") Long productId) {
        return ResponseEntity.ok(stockCommandService.generateOptionCombinationsAndSaveStock(productId));
    }

    @PatchMapping
    public ResponseEntity<UpdatedStock> updateStockQuantity(@RequestBody StockQuantityUpdateRequest request) {
        return ResponseEntity.ok(stockCommandService.updateStockQuantity(request.getProductId(),
                                                                         request.getOptionDetailIds(),
                                                                         request.getQuantity()));
    }

    @PostMapping("/deduct")
    public ResponseEntity<DeductedStockInfo> deductStockQuantity(@RequestBody StockQuantityUpdateRequest request) {
        return ResponseEntity.ok(stockCommandService.deductStockQuantity(request.getProductId(),
                                                                         request.getOptionDetailIds(),
                                                                         request.getQuantity()));
    }

}
