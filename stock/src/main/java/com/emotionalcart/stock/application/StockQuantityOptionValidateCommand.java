package com.emotionalcart.stock.application;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StockQuantityOptionValidateCommand {

    private List<Long> optionDetailsIds;

    private Integer quantity;

}
