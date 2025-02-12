package com.emotionalcart.product.presentation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.emotionalcart.product.presentation.dto.ReadProductDetails;
import com.emotionalcart.product.presentation.dto.ReadProductReviews;
import com.emotionalcart.product.presentation.dto.ReadProductsPrice;
import com.emotionalcart.product.presentation.dto.ReadProductsValidate;
import com.emotionalcart.product.presentation.dto.ReadProducts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "상품 API", description = "상품 관련 API")
public interface ProductControllerDocs {

    // 상품 리뷰 조회
    @GetMapping("/{productId}/reviews")
    ResponseEntity<Page<ReadProductReviews.Response>> readProductReviews(
            @PathVariable Long productId,
            ReadProductReviews.Request request);

    // 상품 상세 조회
    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보를 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadProductDetails.Response.class), examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "핸드폰",
                        "description": "핸드폰 설명",
                        "price": 1500000,
                        "category": {
                            "id": 1,
                            "name": "전자제품",
                            "parentCategoryId": null,
                            "subCategories": [
                                {
                                    "id": 4,
                                    "name": "노트북",
                                    "parentCategoryId": 1,
                                    "subCategories": []
                                },
                                {
                                    "id": 5,
                                    "name": "스마트폰",
                                    "parentCategoryId": 1,
                                    "subCategories": []
                                },
                                {
                                    "id": 6,
                                    "name": "태블릿",
                                    "parentCategoryId": 1,
                                    "subCategories": []
                                }
                            ]
                        },
                        "provider": {
                            "id": 1,
                            "name": "TechProvider",
                            "description": "최고의 기술 제품 제공 업체"
                        },
                        "options": [
                            {
                                "id": 5,
                                "name": "색상",
                                "optionDetails": [
                                    {
                                        "id": 8,
                                        "value": "검정",
                                        "quantity": 50,
                                        "order": 1,
                                        "additionalPrice": 0
                                    },
                                    {
                                        "id": 12,
                                        "value": "은색",
                                        "quantity": 30,
                                        "order": 2,
                                        "additionalPrice": 10000
                                    }
                                ]
                            }
                        ],
                        "reviewStatistic": {
                            "averageRating": 4.5,
                            "reviewCount": 120
                        },
                        "images": [
                            {
                                "id": 1,
                                "fileOrder": 1,
                                "url": "https://example.com/images/main.png",
                                "type": "MAIN"
                            },
                            {
                                "id": 5,
                                "fileOrder": 1,
                                "url": "https://example.com/images/datail.png",
                                "type": "DETAIL"
                            }
                        ]
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "errorCode": "PRODUCT-0006",
                        "errorMessage": "해당 상품을 찾을 수 없습니다."
                    }
                    """)))
    })
    ResponseEntity<ReadProductDetails.Response> getProductDetail(@PathVariable Long productId);

    // 상품 검증
    @PostMapping("/validate")
    ResponseEntity<Void> readProductsValidate(
            @RequestBody @Valid List<ReadProductsValidate.Request> requests);

    // 상품 가격 조회
    @PostMapping("/price")
    ResponseEntity<List<ReadProductsPrice.Response>> readProductsPrice(
            @RequestBody @Valid List<ReadProductsPrice.Request> requests);
                                                                                                  
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
