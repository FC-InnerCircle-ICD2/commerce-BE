package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.presentation.dto.*;
import com.emotionalcart.core.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "백오피스 판매처 (공급자) API", description = "백오피스 판매처 (공급자) 관련 API")
public interface AdminProviderControllerDocs {

    @Operation(
        summary = "판매처 (공급자) 등록",
        description = "판매처 (공급자)를 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "판매처 (공급자) 등록 성공",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateProviderResponse.class),
                    examples = @ExampleObject(name = "판매처 (공급자) 등록 성공", value = "{ \"providerId\": 1 }")
                )),

            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ExceptionResponse.class)))
        }
    )
    ResponseEntity<CreateProviderResponse> createProvider(
        @Parameter(description = "판매처 (공급자) 등록 요청 데이터", required = true)
        @Valid @ModelAttribute CreateProviderRequest createProviderRequest
    );

    @Operation(summary = "판매처 (공급자) 목록 조회")
    public ResponseEntity<Page<ReadAdminProvidersResponse>> readProviders(ReadAdminProvidersRequest request);
}
