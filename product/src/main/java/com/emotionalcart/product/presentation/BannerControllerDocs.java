package com.emotionalcart.product.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.emotionalcart.product.presentation.dto.ReadBanners;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "배너 API", description = "배너 관련 API")
public interface BannerControllerDocs {

    @GetMapping
    @Operation(summary = "배너 목록 조회", description = "모든 배너의 목록을 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "배너 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadBanners.class), examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "type": "PRODUCT",
                        "title": "신년 할인",
                        "bannerOrder": 1,
                        "iconUrl": "/icons/new_year.png",
                        "bannerImage": {
                          "id": 1,
                          "url": "/images/banners/winter_jumper.jpg",
                          "fileOrder": 1
                        },
                        "productBannerResponse": {
                          "id": 1,
                          "linkUrl": "/product/1",
                          "linkType": "INTERNAL"
                        }
                      },
                      {
                        "id": 2,
                        "type": "PRODUCT",
                        "title": "봄맞이 페스티벌",
                        "bannerOrder": 2,
                        "iconUrl": "/icons/spring_festival.png",
                        "bannerImage": {
                          "id": 2,
                          "url": "/images/banners/smartphone_x.jpg",
                          "fileOrder": 1
                        },
                        "productBannerResponse": {
                          "id": 2,
                          "linkUrl": "/product/2",
                          "linkType": "INTERNAL"
                        }
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "PRODUCT-0012", description = "배너를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                        "errorCode": "PRODUCT-0012",
                        "errorMessage": "배너를 찾을 수 없습니다."
                    }
                    """)))
    })
    ResponseEntity<List<ReadBanners.Response>> readBanners();
}
