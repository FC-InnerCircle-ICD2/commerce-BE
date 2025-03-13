package com.emotionalcart.stock.presentation;

import com.emotionalcart.stock.application.StockQuantityValidateCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockQuantityValidateRequest {

    @NotNull(message = "상품 식별자는 필수입니다.")
    private Long productId;

    @NotNull(message = "옵션 상세 목록은 필수입니다.")
    private List<StockQuantityOptionValidateRequest> optionDetails;

    public StockQuantityValidateCommand mapToCommand() {
        return StockQuantityValidateCommand.builder()
            .productId(productId)
            .optionValidateCommands(optionDetails.stream().map(StockQuantityOptionValidateRequest::mapToCommand).toList())
            .build();
    }

}
