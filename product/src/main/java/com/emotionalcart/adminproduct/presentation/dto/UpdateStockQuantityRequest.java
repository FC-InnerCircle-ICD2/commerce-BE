package com.emotionalcart.adminproduct.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateStockQuantityRequest {

    @Schema(description = "옵션 상세 항목")
    @NotNull
    @Size(min = 1, message = "옵션 아이디 배열을 입력해 주세요.")
    private List<Long> optionDetailIds;

    @Schema(description = "변경할 재고 수량")
    @NotNull(message = "수량은 필수 값입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private Integer quantity;

}
