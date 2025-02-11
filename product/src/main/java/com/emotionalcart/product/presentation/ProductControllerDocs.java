package com.emotionalcart.product.presentation;

import com.emotionalcart.product.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 API", description = "상품 관련 API")
public interface ProductControllerDocs {

    @Operation(summary = "상품 리뷰 조회", description = "특정 상품의 리뷰 목록을 페이징하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 리뷰 조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReadProductReviews.Response.class),
                    examples = @ExampleObject(value = """
                            {
                                "content": [
                                    {
                                        "id": 1,
                                        "productName": "Nike Air Max",
                                        "productOptionName": "270mm",
                                        "rating": 5,
                                        "content": "아주 만족스러운 제품입니다.",
                                        "createdAt": "2024-01-29T12:00:00",
                                        "reviewImages": [
                                            {
                                                "id": 101,
                                                "url": "https://your-s3-bucket.s3.amazonaws.com/images/products/100/photo1.jpg",
                                                "fileOrder": 1
                                            }
                                        ]
                                    }
                                ],
                                "pageable": {
                                    "pageNumber": 0,
                                    "pageSize": 10
                                },
                                "totalElements": 1,
                                "totalPages": 1,
                                "last": true
                            }
                            """
                    ))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "errorCode": "PRODUCT-006",
                                        "errorMessage": "상품을 찾을 수 없습니다."
                                    }
                                    """
                            )
                    ))
    })
    ResponseEntity<Page<ReadProductReviews.Response>> readProductReviews(
            @PathVariable Long productId,
            ReadProductReviews.Request request);

    @Operation(summary = "상품 리뷰 등록", description = "특정 상품의 리뷰를 등록합니다.")
    ResponseEntity<CreateProductReview.Response> createProductReview(
            @PathVariable Long productId,
            @ModelAttribute @Valid CreateProductReview.Request request);

    // 상품 상세 조회
    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보를 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadProductDetails.Response.class), examples = @ExampleObject(value = """
                    {
                        "options": [
                            {
                                "optionDetails": [
                                    {
                                        "images": [
                                            {
                                                "id": 10,
                                                "fileOrder": 1,
                                                "url": "https://your-s3-bucket.s3.amazonaws.com/images/products/100/photo1.jpg",
                                                "representative": true
                                            }
                                        ],
                                        "id": 1,
                                        "value": "256GB",
                                        "quantity": 20,
                                        "order": 1,
                                        "additionalPrice": 10000
                                    },
                                    {
                                        "images": [
                                            {
                                                "id": 10,
                                                "fileOrder": 1,
                                                "url": "https://your-s3-bucket.s3.amazonaws.com/images/products/100/photo1.jpg",
                                                "representative": true
                                            }
                                        ],
                                        "id": 2,
                                        "value": "512GB",
                                        "quantity": 10,
                                        "order": 2,
                                        "additionalPrice": 20000
                                    }
                                ],
                                "id": 1,
                                "name": "저장용량"
                            }
                        ],
                        "id": 100,
                        "name": "노트북 X1",
                        "description": "고성능 노트북입니다.",
                        "price": 1599000,
                        "category": {
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
                        "provider": {
                            "id": 10,
                            "name": "ABC 전자",
                            "description": "전자제품 전문 업체입니다."
                        },
                        "reviewStatistic": {
                            "averageRating": 4.5,
                            "reviewCount": 120
                        }
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
