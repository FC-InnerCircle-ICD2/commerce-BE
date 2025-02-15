package com.emotionalcart.adminproduct.presentation;

import com.emotionalcart.adminproduct.presentation.dto.CreateProduct;
import com.emotionalcart.core.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "백오피스 상품 API", description = "백오피스 상품 관련 API")
public interface AdminProductControllerDocs {

    @Operation(
            summary = "상품 등록",
            description = "상품을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 등록 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateProduct.Response.class),
                                    examples = @ExampleObject(name = "상품 등록 성공", value = "{ \"productId\": 1 }")
                            )),

                    @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ExceptionResponse.class)))
            }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상품 등록 요청 데이터",
            required = true,
            content = @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(implementation = CreateProduct.Request.class),
                    examples = @ExampleObject(
                            name = "상품 등록 예시",
                            description = "상품 등록 요청 예시 데이터",
                            value = "{"
                                    + "\"name\": \"아이패드 프로\","
                                    + "\"description\": \"최신 M2 칩 탑재\","
                                    + "\"price\": 1500000,"
                                    + "\"providerId\": 4,"
                                    + "\"categoryId\": 6,"
                                    + "\"options\": [{"
                                    + "   \"name\": \"저장 용량\","
                                    + "   \"optionDetails\": [{"
                                    + "       \"value\": \"128GB\","
                                    + "       \"optionOrder\": 1,"
                                    + "       \"additionalPrice\": 0"
                                    + "   }]"
                                    + "}],"
                                    + "\"mainImage\": \"(파일 업로드)\","
                                    + "\"detailImages\": [\"(파일 업로드)\", \"(파일 업로드)\"]"
                                    + "}"
                    )
            )
    )
    ResponseEntity<CreateProduct.Response> createProduct(@Valid @ModelAttribute CreateProduct.Request request);
}
