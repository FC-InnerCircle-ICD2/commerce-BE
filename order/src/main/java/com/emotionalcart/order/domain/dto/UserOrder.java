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

        private List<ProductOption> productOptions;

        public OrderProduct(OrderItem orderItem, ProductDetail productDetail) {
            this.productId = orderItem.getProductId();
            this.providerId = productDetail.getProviderId();
            this.productName = productDetail.getProductName();
            this.providerName = productDetail.getProviderName();
            this.productImage = productDetail.getProductImage();
            this.productPrice = orderItem.getItemPrice();
            this.quantity = orderItem.getQuantity();
            this.productOptions = productDetail.getProductOptions().stream().map(ProductOption::from).toList();
        }

        public static OrderProduct from(OrderItem orderItem, ProductDetail productDetail) {
            return new OrderProduct(orderItem, productDetail);
        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class ProductOption {

            private Long productOptionId;

            private String productOptionName;

            private Long productOptionDetailId;

            private String productOptionDetailName;

            public ProductOption(Long id, String name, List<ProductDetail.ProductOptionDetail> optionDetails) {
                this.productOptionId = id;
                this.productOptionName = name;

                if (!optionDetails.isEmpty()) {
                    ProductDetail.ProductOptionDetail productOptionDetail = optionDetails.getFirst();
                    this.productOptionDetailId = productOptionDetail.getId();
                    this.productOptionDetailName = productOptionDetail.getValue();
                }
            }

            public static ProductOption from(ProductDetail.ProductDetailOption productDetailOption) {
                return new ProductOption(productDetailOption.getId(),
                        productDetailOption.getName(),
                        productDetailOption.getOptionDetails());
            }

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class ProductOptionDetail {

            private Long productOptionDetailId;

            private String productOptionDetailName;

            public ProductOptionDetail(ProductDetail.ProductOptionDetail productOptionDetail) {
                this.productOptionDetailId = productOptionDetail.getId();
                this.productOptionDetailName = productOptionDetail.getValue();
            }

            public static ProductOptionDetail from(ProductDetail.ProductOptionDetail productOptionDetail) {
                return new ProductOptionDetail(productOptionDetail);
            }

        }

    }

}
