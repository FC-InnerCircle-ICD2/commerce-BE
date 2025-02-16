package com.emotionalcart.stock.presentation;

import com.emotionalcart.stock.application.StockQuantityValidateCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockQuantityValidateRequest {

    private Long productId;

    private List<StockQuantityOptionValidateRequest> optionDetails;

    public StockQuantityValidateCommand mapToCommand() {
        return StockQuantityValidateCommand.builder()
            .productId(productId)
            .optionValidateCommands(optionDetails.stream().map(StockQuantityOptionValidateRequest::mapToCommand).toList())
            .build();
    }

}
