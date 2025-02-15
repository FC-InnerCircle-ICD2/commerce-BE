package com.emotionalcart.shipment.presentation.controller.request;

import com.emotionalcart.shipment.domain.dto.CreateShipment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderShipmentRequest {

    /**
     * 주문 번호
     */
    @NotNull(message = "주문 번호는 필수입니다.")
    private Long orderId;

    /**
     * 업체 번호
     * 주문 한 건에 여러 상품이 들어올 수가 있음
     * 해당 상품 들은 각자 업체가 다를 수가 있음
     */
    @NotNull(message = "업체 번호는 필수입니다.")
    private List<Long> providerIds;

    /**
     * 배송 정본
     */
    @Valid
    @NotNull(message = "배송 정보는 필수 입니다.")
    private DeliveryInfo deliveryInfo;

    public CreateShipment mapToDomain() {
        return CreateShipment.from(this);
    }

    /**
     * 배송 정보
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeliveryInfo {

        @NotBlank(message = "수령인 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "전화번호는 필수입니다.")
        private String phoneNumber;

        @NotBlank(message = "우편번호는 필수입니다.")
        private String zoneCode;

        @NotBlank(message = "주소는 필수입니다.")
        private String address;

        @NotBlank(message = "상세 주소는 필수입니다.")
        private String detailAddress;

        /**
         * 배송 메모 선택 값
         */
        private String deliveryMemo;

    }

}
