package com.emotionalcart.order.presentation.controller.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "주문 API", description = "사용자의 주문 정보를 조회하는 API")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "주문 목록 조회 응답")
public class GetOrderListResponse {

    @Schema(description = "주문 번호", example = "4002610742914454480")
    private String orderId;

    @ArraySchema(schema = @Schema(implementation = OrderProduct.class))
    @Schema(description = "주문한 상품 목록")
    private List<OrderProduct> orderProductList;

    @Schema(description = "총 주문 금액", example = "1050000")
    private double totalOrderPrice;

    @Schema(description = "주문 날짜", example = "2025-02-14T12:00:00")
    private LocalDateTime orderDate;

    @Schema(description = "주문 상태", example = "배송중")
    private String orderStatus;

    public static GetOrderListResponse from() {
        return new GetOrderListResponse();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "주문한 상품 정보")
    public static class OrderProduct {

        @Schema(description = "상품 ID", example = "4002396260259129221")
        private String productId;

        @Schema(description = "상품명", example = "블랙 자켓")
        private String productName;

        @Schema(description = "상품 이미지 URL", example = "https://imageurl.com/jacket.jpg")
        private String productImage;

        @Schema(description = "상품 총 금액", example = "55000")
        private double productPrice;

        @Schema(description = "구매 수량", example = "1")
        private int quantity;

    }

}