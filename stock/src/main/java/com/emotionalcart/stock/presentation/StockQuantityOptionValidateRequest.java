package com.emotionalcart.stock.presentation;

import com.emotionalcart.stock.application.StockQuantityOptionValidateCommand;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StockQuantityOptionValidateRequest {

    private List<Long> optionDetailsIds;

    private Integer quantity;

    public StockQuantityOptionValidateCommand mapToCommand() {
        return StockQuantityOptionValidateCommand.builder()
            .optionDetailsIds(optionDetailsIds)
            .quantity(quantity)
            .build();
    }

}
