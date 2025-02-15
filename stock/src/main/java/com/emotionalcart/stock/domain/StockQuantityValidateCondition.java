package com.emotionalcart.stock.domain;

import com.emotionalcart.stock.application.StockQuantityOptionValidateCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantityValidateCondition {

    private Long productId;
    private List<StockQuantityOptionValidateCommand> optionValidateCommands;

    public static StockQuantityValidateCondition of(Long productId, List<StockQuantityOptionValidateCommand> optionValidateCommands) {
        return new StockQuantityValidateCondition(productId, optionValidateCommands);
    }

}
