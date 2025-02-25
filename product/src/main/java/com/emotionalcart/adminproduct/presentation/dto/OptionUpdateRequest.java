package com.emotionalcart.adminproduct.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OptionUpdateRequest {

    @Schema(description = "id 있으면 수정 / 없으면 추가")
    private Long id;
    @NotNull(message = "옵션 이름은 필수입니다.")
    private String name;
    @Schema(description = "옵션 상세 항목")
    private List<OptionDetailUpdateRequest> details = new ArrayList<>();

}