package com.emotionalcart.product.presentation;

import com.emotionalcart.common.jwt.JwtAuthentication;
import com.emotionalcart.product.presentation.dto.*;
import com.emotionalcart.product.presentation.dto.request.CreateProductReviewRequest;
import com.emotionalcart.product.presentation.dto.response.CreateProductReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 API", description = "상품 관련 API")
public interface ProductControllerDocs {

    // 상품 리뷰 조회
    @GetMapping("/{productId}/reviews") ResponseEntity<Page<ReadProductReviews.Response>> readProductReviews(
        @PathVariable Long productId,
        ReadProductReviews.Request request);

    // 상품 리뷰 등록
    @PostMapping(value = "/{productId}/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) ResponseEntity<CreateProductReviewResponse> createProductReview(
        @AuthenticationPrincipal JwtAuthentication jwt,
        @PathVariable Long productId,
        @ModelAttribute @Valid CreateProductReviewRequest request);

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
                          "productId": 3,
                          "name": "러닝화",
                          "description": "가볍고 편안한 러닝화",
                          "price": 25000,
                          "category": {
                              "id": 2,
                              "name": "패션",
                              "parentCategoryId": null,
                              "subCategories": [
                                  {
                                      "id": 7,
                                      "name": "남성 의류",
                                      "parentCategoryId": 2,
                                      "subCategories": []
                                  },
                                  {
                                      "id": 8,
                                      "name": "여성 의류",
                                      "parentCategoryId": 2,
                                      "subCategories": []
                                  }
                              ]
                          },
                          "provider": {
                              "id": 2,
                              "name": "FashionWorld",
                              "description": "모든 사람을 위한 스타일리시한 의류"
                          },
                          "options": [
                              {
                                  "id": 7,
                                  "name": "사이즈",
                                  "optionDetails": [
                                      {
                                          "id": 5,
                                          "value": "Small",
                                          "quantity": 100,
                                          "order": 1,
                                          "additionalPrice": 0
                                      },
                                      {
                                          "id": 6,
                                          "value": "Large",
                                          "quantity": 50,
                                          "order": 2,
                                          "additionalPrice": 0
                                      },
                                      {
                                          "id": 7,
                                          "value": "Medium",
                                          "quantity": 50,
                                          "order": 3,
                                          "additionalPrice": 0
                                      }
                                  ]
                              }
                          ],
                          "rating": 4.2,
                          "images": [
                              {
                                  "id": 3,
                                  "fileOrder": 1,
                                  "url": "https://example.com/images/main.png",
                                  "type": "MAIN"
                              }
                          ],
                          "isDeleted": false
                      },
                    {
                         "productId": 2,
                         "name": "스마트폰 X",
                         "description": "최신 기능이 탑재된 스마트폰",
                         "price": 200000,
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
                                 "id": 6,
                                 "name": "저장 용량",
                                 "optionDetails": [
                                     {
                                         "id": 10,
                                         "value": "256GB",
                                         "quantity": 20,
                                         "order": 3,
                                         "additionalPrice": 20000
                                     },
                                     {
                                         "id": 11,
                                         "value": "512GB",
                                         "quantity": 10,
                                         "order": 4,
                                         "additionalPrice": 40000
                                     }
                                 ]
                             }
                         ],
                         "rating": 4.8,
                         "images": [
                             {
                                 "id": 2,
                                 "fileOrder": 1,
                                 "url": "https://example.com/images/main.png",
                                 "type": "MAIN"
                             }
                         ],
                         "isDeleted": true
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
