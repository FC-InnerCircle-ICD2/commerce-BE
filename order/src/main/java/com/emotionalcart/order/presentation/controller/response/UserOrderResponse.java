package com.emotionalcart.order.presentation.controller.response;

import com.emotionalcart.order.domain.dto.UserOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class UserOrderResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "주문 번호", example = "4002610742914454480")
    private Long orderId;

    @ArraySchema(schema = @Schema(implementation = OrderProduct.class))
    @Schema(description = "주문한 상품 목록")
    private List<OrderProduct> orderProductList;

    @Schema(description = "총 주문 금액", example = "1050000")
    private double totalOrderPrice;

    @Schema(description = "주문 날짜", example = "2025-02-14T12:00:00")
    private LocalDateTime orderAt;

    @Schema(description = "주문 상태", example = "배송중")
    private String orderStatus;

    public UserOrderResponse(Long orderId,
                             List<OrderProduct> orderProductList,
                             double totalOrderPrice,
                             LocalDateTime orderAt,
                             String orderStatus) {
        this.orderId = orderId;
        this.orderProductList = orderProductList;
        this.totalOrderPrice = totalOrderPrice;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
    }

    public UserOrderResponse(Long id, double totalPrice, LocalDateTime orderAt, String status, List<OrderProduct> orderProducts) {
        this.orderId = id;
        this.orderProductList = orderProducts;
        this.totalOrderPrice = totalPrice;
        this.orderAt = orderAt;
        this.orderStatus = status;
    }

    public UserOrderResponse(UserOrder userOrder) {
        this.orderId = userOrder.getOrderId();
        this.orderStatus = userOrder.getOrderStatus();
        this.orderAt = userOrder.getOrderAt();
        this.orderProductList = userOrder.getOrderProductList().stream().map(OrderProduct::from).toList();
    }

    public static UserOrderResponse of(Long id,
                                       double totalPrice,
                                       LocalDateTime orderAt,
                                       String status,
                                       List<OrderProduct> orderProducts) {
        return new UserOrderResponse(id, totalPrice, orderAt, status, orderProducts);
    }

    public static UserOrderResponse from(UserOrder userOrder) {
        return new UserOrderResponse(userOrder);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "주문한 상품 정보")
    public static class OrderProduct {

        @JsonSerialize(using = ToStringSerializer.class)
        @Schema(description = "상품 ID", example = "4002396260259129221")
        private Long productId;

        @JsonSerialize(using = ToStringSerializer.class)
        @Schema(description = "업체 ID", example = "123141511413412")
        private Long providerId;

        @Schema(description = "업체 명", example = "쿠팡")
        private String providerName;

        @Schema(description = "상품명", example = "블랙 자켓")
        private String productName;

        @Schema(description = "상품 이미지 URL", example = "https://imageurl.com/jacket.jpg")
        private String productImage;

        @Schema(description = "상품 총 금액", example = "55000")
        private double productPrice;

        @Schema(description = "구매 수량", example = "1")
        private int quantity;

        public OrderProduct(Long productId, String productName, int quantity, double orderItemPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.productPrice = orderItemPrice;
        }

        public OrderProduct(UserOrder.OrderProduct orderProduct) {
            this.productId = orderProduct.getProductId();
            this.productName = orderProduct.getProductName();
            this.providerId = orderProduct.getProviderId();
            this.providerName = orderProduct.getProviderName();
            this.productPrice = orderProduct.getProductPrice();
            this.productImage = orderProduct.getProductImage();
            this.quantity = orderProduct.getQuantity();
        }

        public static OrderProduct of(Long productId, String productName, int quantity, double orderItemPrice) {
            return new OrderProduct(productId, productName, quantity, orderItemPrice);
        }

        public static OrderProduct from(UserOrder.OrderProduct orderProduct) {
            return new OrderProduct(orderProduct);
        }

    }

}