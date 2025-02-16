package com.emotionalcart.stock.application;

import com.emotionalcart.stock.domain.StockQuantityValidateCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantityValidateCommand {

    private Long productId;
    private List<StockQuantityOptionValidateCommand> optionValidateCommands;
    private Integer quantity;

    public StockQuantityValidateCondition mapToCondition() {
        return StockQuantityValidateCondition.builder().productId(productId).optionValidateCommands(optionValidateCommands).build();
    }

}
