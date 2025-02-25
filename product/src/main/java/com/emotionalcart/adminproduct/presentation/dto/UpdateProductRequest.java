package com.emotionalcart.adminproduct.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateProductRequest {

    @Schema(description = "상품명")
    @NotNull(message = "상품명을 입력해주세요")
    private String name;
    @Schema(description = "가격")
    @NotNull(message = "가격을 입력해주세요")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;
    @Schema(description = "설명")
    @NotNull(message = "설명을 입력해주세요")
    private String description;
    @Valid
    @Schema(description = "옵션 목록 (id 있으면 수정 / 없으면 추가)")
    private List<OptionUpdateRequest> options = new ArrayList<>();
    @Schema(description = "삭제할 옵션 ID 목록")
    private List<Long> deletedOptionIds = new ArrayList<>();
    @Schema(description = "삭제할 옵션 상세 항목 ID 목록")
    private List<Long> deletedDetailIds = new ArrayList<>();

}