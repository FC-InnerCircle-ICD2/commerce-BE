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
            @RequestBody @Valid List<ReadProductsValidate.Request> requests
    );

    // 상품 가격 조회
    @PostMapping("/price")
    ResponseEntity<List<ReadProductsPrice.Response>> readProductsPrice(
            @RequestBody @Valid List<ReadProductsPrice.Request> requests
    );
}
