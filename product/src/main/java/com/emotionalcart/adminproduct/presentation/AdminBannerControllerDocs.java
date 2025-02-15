package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.presentation.dto.CreateBannerRequest;
import com.emotionalcart.adminproduct.presentation.dto.CreateBannerResponse;
import com.emotionalcart.core.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "백오피스 배너 API", description = "백오피스 배너 관련 API")
public interface AdminBannerControllerDocs {

    @Operation(
            summary = "배너 등록",
            description = "배너를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "배너 등록 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateBannerResponse.class),
                                    examples = @ExampleObject(name = "배너 등록 성공", value = "{ \"bannerId\": 1 }")
                            )),

                    @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ExceptionResponse.class)))
            }
    )
    ResponseEntity<CreateBannerResponse> createBanner(
            @Parameter(description = "배너 등록 요청 데이터", required = true)
            @ModelAttribute @Valid CreateBannerRequest request
    );
}
