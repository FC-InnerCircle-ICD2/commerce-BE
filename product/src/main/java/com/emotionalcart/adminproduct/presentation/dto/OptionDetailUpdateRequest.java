package com.emotionalcart.adminproduct.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OptionDetailUpdateRequest {

    @Schema(description = "id 있으면 수정 / 없으면 추가")
    private Long id;
    @NotNull(message = "옵션 상세 값은 필수입니다.")
    private String value;
    @NotNull(message = "옵션 순서 값은 필수입니다.")
    private Integer optionOrder;
    private Integer additionalPrice = 0;

}