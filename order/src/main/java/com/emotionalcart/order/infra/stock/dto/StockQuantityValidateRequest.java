package com.emotionalcart.order.infra.stock.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 수량 조회 요청
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockQuantityValidateRequest {

    private Long productId;

    private List<StockQuantityOptionValidateRequest> optionDetails;

    public StockQuantityValidateRequest(Long productId, List<StockQuantityOptionValidateRequest> orDefault) {
        this.productId = productId;
        this.optionDetails = orDefault;
    }

    public static StockQuantityValidateRequest of(Long productId, List<StockQuantityOptionValidateRequest> orDefault) {
        return new StockQuantityValidateRequest(productId, orDefault);
    }

}
