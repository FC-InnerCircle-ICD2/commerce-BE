package com.emotionalcart.product.presentation;

import com.emotionalcart.product.presentation.dto.ReadProducts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface ProductControllerDocs {

    //상품 목록 조회
    @GetMapping("/search")
    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadProducts.Response.class), examples = @ExampleObject(value = """
                    {
                        "content": [
                            {
                                "productId": 6,
                                "name": "스타일리시한 티셔츠",
                                "description": "트렌디하고 편안한 티셔츠",
                                "price": 25000,
                                "category": {
                                    "productCategoryId": 2,
                                    "name": "패션",
                                    "parentProductCategoryId": null,
                                    "subProductCategories": [
                                        {
                                            "productCategoryId": 7,
                                            "name": "남성 의류",
                                            "parentProductCategoryId": 2,
                                            "subProductCategories": []
                                        },
                                        {
                                            "productCategoryId": 8,
                                            "name": "여성 의류",
                                            "parentProductCategoryId": 2,
                                            "subProductCategories": []
                                        }
                                    ]
                                },
                                "provider": {
                                    "providerId": 2,
                                    "name": "FashionWorld",
                                    "description": "모든 사람을 위한 스타일리시한 의류"
                                },
                                "options": [
                                    {
                                        "id": 1,
                                        "name": "색상",
                                        "optionDetails": [
                                            {
                                                "value": "검정",
                                                "quantity": 50,
                                                "additionalPrice": 0,
                                                "fileOrder": 1,
                                                "url": "https://example.com/images/laptop_black.png"
                                            },
                                            {
                                                "value": "은색",
                                                "quantity": 30,
                                                "additionalPrice": 10000,
                                                "fileOrder": 2,
                                                "url": "https://example.com/images/laptop_silver.png"
                                            }
                                        ]
                                    },
                                    {
                                        "id": 3,
                                        "name": "사이즈",
                                        "optionDetails": [
                                            {
                                                "value": "Small",
                                                "quantity": 100,
                                                "additionalPrice": 0,
                                                "fileOrder": 1,
                                                "url": "https://example.com/images/tshirt_small.png"
                                            },
                                            {
                                                "value": "Large",
                                                "quantity": 50,
                                                "additionalPrice": 0,
                                                "fileOrder": 2,
                                                "url": "https://example.com/images/tshirt_large.png"
                                            },
                                            {
                                                "value": "Medium",
                                                "quantity": 50,
                                                "additionalPrice": 0,
                                                "fileOrder": 2,
                                                "url": "https://example.com/images/tshirt_medium.png"
                                            }
                                        ]
                                    }
                                ],
                                "rating": 4.8
                            },
                            {
                                "productId": 3,
                                "name": "러닝화",
                                "description": "가볍고 편안한 러닝화",
                                "price": 25000,
                                "category": {
                                    "productCategoryId": 2,
                                    "name": "패션",
                                    "parentProductCategoryId": null,
                                    "subProductCategories": [
                                        {
                                            "productCategoryId": 7,
                                            "name": "남성 의류",
                                            "parentProductCategoryId": 2,
                                            "subProductCategories": []
                                        },
                                        {
                                            "productCategoryId": 8,
                                            "name": "여성 의류",
                                            "parentProductCategoryId": 2,
                                            "subProductCategories": []
                                        }
                                    ]
                                },
                                "provider": {
                                    "providerId": 2,
                                    "name": "FashionWorld",
                                    "description": "모든 사람을 위한 스타일리시한 의류"
                                },
                                "options": [
                                    {
                                        "id": 1,
                                        "name": "색상",
                                        "optionDetails": [
                                            {
                                                "value": "검정",
                                                "quantity": 50,
                                                "additionalPrice": 0,
                                                "fileOrder": 1,
                                                "url": "https://example.com/images/laptop_black.png"
                                            },
                                            {
                                                "value": "은색",
                                                "quantity": 30,
                                                "additionalPrice": 10000,
                                                "fileOrder": 2,
                                                "url": "https://example.com/images/laptop_silver.png"
                                            }
                                        ]
                                    }
                                ],
                                "rating": 4.2
                            }
                        ],
                        "page": {
                            "size": 10,
                            "number": 0,
                            "totalElements": 3,
                            "totalPages": 1
                        }
                    }
                    """)))
    })
    ResponseEntity<Page<ReadProducts.Response>> readProducts(ReadProducts.Request request);
}
