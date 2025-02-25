package com.emotionalcart.product.domain.support;

import com.emotionalcart.core.feature.product.ProductOptionDetail;
import com.emotionalcart.product.infrastructure.stock.dto.OptionStockDto;
import lombok.Getter;

import java.util.List;

@Getter
public class OptionDetailsGroup {
    private final List<OptionStockDto> options;

    private OptionDetailsGroup(List<OptionStockDto> options) {
        this.options = options;
    }

    public List<Long> getOptionIds() {
        return options.stream().map(OptionStockDto::getId).toList();
    }

    public static OptionDetailsGroup from(List<ProductOptionDetail> productOptionDetails) {
        List<OptionStockDto> details = productOptionDetails.stream()
                .map(detail -> new OptionStockDto(detail.getId(), detail.getValue()))
                .toList();
        return new OptionDetailsGroup(details);
    }

    public static OptionDetailsGroup fromDetails(List<OptionStockDto> optionDetails) {
        return new OptionDetailsGroup(optionDetails);
    }
}
