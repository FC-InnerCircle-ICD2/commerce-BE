package com.emotionalcart.product.infrastructure.stock.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockQuantityValidateRequest {
    private Long productId;

    private List<StockQuantityOptionValidateRequest> optionDetails;

    public StockQuantityValidateRequest(Long productId, List<StockQuantityOptionValidateRequest> optionDetails) {
        this.productId = productId;
        this.optionDetails = optionDetails;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StockQuantityOptionValidateRequest {

        private List<Long> optionDetailsIds;

        private Integer quantity;

        public StockQuantityOptionValidateRequest(List<Long> optionDetails, Integer quantity) {
            this.optionDetailsIds = optionDetails;
            this.quantity = quantity;
        }

        public static StockQuantityOptionValidateRequest of(int quantity, List<Long> optionDetailsIds) {
            return new StockQuantityOptionValidateRequest(optionDetailsIds, quantity);
        }
    }

    public static StockQuantityValidateRequest of(Long productId, List<StockQuantityOptionValidateRequest> optionDetails) {
        return new StockQuantityValidateRequest(productId, optionDetails);
    }

}
