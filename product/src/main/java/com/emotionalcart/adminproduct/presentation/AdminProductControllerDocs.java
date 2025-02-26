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
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "백오피스 상품 API", description = "백오피스 상품 관련 API")
public interface AdminProductControllerDocs {

    @Operation(
        summary = "상품 등록",
        description = "상품을 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "상품 등록 성공",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateProductResponse.class),
                    examples = @ExampleObject(name = "상품 등록 성공", value = "{ \"productId\": 1 }")
                )),

            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ExceptionResponse.class)))
        }
    )
    ResponseEntity<CreateProductResponse> createProduct(
        @Parameter(description = "상품 등록 요청 데이터", required = true)
        @Valid @ModelAttribute CreateProductRequest createProductRequest
    );

    @Operation(summary = "상품 삭제")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId);

    @Operation(summary = "상품 목록 조회")
    public ResponseEntity<Page<ReadAdminProductsResponse>> readProducts(ReadAdminProductsRequest request);

    @Operation(summary = "상품 상세 조회")
    public ResponseEntity<ReadAdminProductDetailResponse> readProduct(@PathVariable Long productId);

}
