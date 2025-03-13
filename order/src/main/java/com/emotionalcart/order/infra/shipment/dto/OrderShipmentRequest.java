package com.emotionalcart.order.infra.shipment.dto;

import com.emotionalcart.order.domain.entity.OrderRecipient;
import com.emotionalcart.order.infra.product.dto.ProductPrice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderShipmentRequest {

    private Long orderId;

    private List<ProviderProductRequest> providerProductRequests;

    private DeliveryInfo deliveryInfo;

    public OrderShipmentRequest(Long orderId, OrderRecipient orderRecipient, List<ProductPrice> productPriceList) {
        this.orderId = orderId;
        this.deliveryInfo = DeliveryInfo.from(orderRecipient);
        this.providerProductRequests = productPriceList.stream().map(p -> ProviderProductRequest.from(p.getProviderId())).toList();
    }

    public static OrderShipmentRequest of(Long id, OrderRecipient orderRecipient, List<ProductPrice> productPriceList) {
        return new OrderShipmentRequest(id, orderRecipient, productPriceList);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeliveryInfo {

        private String name;

        private String phoneNumber;

        private String zoneCode;

        private String address;

        private String detailAddress;

        private String deliveryMemo;

        public DeliveryInfo(String recipientName,
                            String recipientPhone,
                            String postalCode,
                            String defaultAddress,
                            String detailAddress,
                            String deliveryRequirement) {
            this.name = recipientName;
            this.phoneNumber = recipientPhone;
            this.zoneCode = postalCode;
            this.address = defaultAddress;
            this.detailAddress = detailAddress;
            this.deliveryMemo = deliveryRequirement;
        }

        public static DeliveryInfo from(OrderRecipient orderRecipient) {
            return new DeliveryInfo(orderRecipient.getRecipientName(),
                                    orderRecipient.getRecipientPhone(),
                                    orderRecipient.getAddress().getPostalCode(),
                                    orderRecipient.getAddress().getDefaultAddress(),
                                    orderRecipient.getAddress().getDetailAddress(),
                                    orderRecipient.getDeliveryRequirement());
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProviderProductRequest {

        private Long providerId;

        public ProviderProductRequest(Long providerId) {
            this.providerId = providerId;
        }

        public static ProviderProductRequest from(Long providerId) {
            return new ProviderProductRequest(providerId);
        }

    }

}