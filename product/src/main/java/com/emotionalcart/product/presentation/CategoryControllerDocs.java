package com.emotionalcart.product.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.emotionalcart.product.presentation.dto.ReadCategories;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카테고리 API", description = "카테고리 관련 API")
public interface CategoryControllerDocs {

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadCategories.Response.class), examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "name": "전자제품",
                            "parentCategoryId": null,
                            "subCategories": [
                                {
                                    "id": 2,
                                    "name": "스마트폰",
                                    "parentCategoryId": 1,
                                    "subCategories": []
                                },
                                {
                                    "id": 3,
                                    "name": "노트북",
                                    "parentCategoryId": 1,
                                    "subCategories": []
                                }
                            ]
                        },
                        {
                            "id": 4,
                            "name": "가전제품",
                            "parentCategoryId": null,
                            "subCategories": []
                        }
                    ]
                    """))),
            @ApiResponse(responseCode = "PRODUCT-0008", description = "카테고리를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "errorCode": "PRODUCT-0008",
                        "errorMessage": "카테고리를 찾을 수 없습니다."
                    }
                    """)))
    })
    ResponseEntity<List<ReadCategories.Response>> getAllCategories();

}
