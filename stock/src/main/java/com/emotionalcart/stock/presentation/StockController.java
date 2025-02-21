package com.emotionalcart.stock.presentation;

import com.emotionalcart.stock.application.*;
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
    public ResponseEntity<Integer> getStockQuantity(@RequestBody StockQuantitySearchRequest request) {
        return ResponseEntity.ok(stockQueryService.getStockQuantityByOptionIds(request.mapToQuery()));
    }

    @PostMapping("/quantity/validate")
    public ResponseEntity<Boolean> getStockQuantities(@RequestBody StockQuantityValidateRequest requests) {
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

    @PatchMapping("/deduct")
    public ResponseEntity<DeductedStockInfo> deductStockQuantity(@RequestBody StockQuantityUpdateRequest request) {
        return ResponseEntity.ok(stockCommandService.deductStockQuantity(request.getProductId(),
                                                                         request.getOptionDetailIds(),
                                                                         request.getQuantity()));
    }

}
