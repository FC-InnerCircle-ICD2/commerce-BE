package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.domain.entity.OrderItem;
import com.emotionalcart.order.domain.entity.Orders;
import com.emotionalcart.order.infra.product.dto.ProductDetail;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOrder {

    /**
     * 주문번호
     */
    private Long orderId;

    /**
     * 최종 금액
     */
    private double totalPrice;

    /**
     * 주문 상태
     */
    private String orderStatus;

    private LocalDateTime orderAt;

    private List<OrderProduct> orderProductList;

    public UserOrder(Orders orders, List<ProductDetail> productDetails) {
        this.orderId = orders.getId();
        this.totalPrice = orders.getTotalPrice();
        this.orderStatus = orders.getStatus().getStatusName();
        this.orderAt = orders.getOrderAt();
        this.orderProductList =
            IntStream.range(0, orders.getOrderItems().size()).mapToObj(i -> OrderProduct.from(orders.getOrderItems().get(i),
                                                                                              productDetails.get(i))).toList();
    }

    public static UserOrder from(Orders orders, List<ProductDetail> productDetails) {
        return new UserOrder(orders, productDetails);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderProduct {

        private Long productId;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long providerId;

        private String providerName;

        private String productName;

        private String productImage;

        private double productPrice;

        private int quantity;

        public OrderProduct(OrderItem orderItem, ProductDetail productDetail) {
            this.productId = orderItem.getProductId();
            this.providerId = productDetail.getProviderId();
            this.productName = productDetail.getProductName();
            this.providerName = productDetail.getProviderName();
            this.productImage = productDetail.getProductImage();
            this.productPrice = orderItem.getItemPrice();
            this.quantity = orderItem.getQuantity();
        }

        public static OrderProduct from(OrderItem orderItem, ProductDetail productDetail) {
            return new OrderProduct(orderItem, productDetail);
        }

    }

}
