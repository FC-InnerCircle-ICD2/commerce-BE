package com.emotionalcart.order.domain.dto;

import com.emotionalcart.order.infra.validator.ValidPhoneNumber;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryInfo extends SelfValidation<DeliveryInfo> {

    /**
     * 수령인 이름
     */
    @NotNull(message = "수령인 이름을 입력해주세요.")
    private String name;
    /**
     * 수령인 전화번호
     */
    @NotNull(message = "수령인 전화번호를 입력해주세요.")
    @ValidPhoneNumber
    private String phoneNumber;

    /**
     * 우편번호
     */
    @NotNull(message = "우편번호를 입력해주세요.")
    private String zonecode;
    /**
     * 배송지 주소
     */
    @NotNull(message = "주소를 입력해주세요.")
    private String address;

    /**
     * 상세주소
     */
    @NotNull(message = "상세주소를 입력해주세요.")
    private String detailAddress;

    /**
     * 배송 메모
     */
    @NotNull(message = "배송 메모를 입력해주세요.")
    private String deliveryMemo;

    public static DeliveryInfo createDeliveryInfo(String name,
                                                  String phone,
                                                  String zoneCode,
                                                  String address,
                                                  String detailAddress,
                                                  String deliveryMemo) {
        return DeliveryInfo.builder()
            .name(name)
            .phoneNumber(phone)
            .zonecode(zoneCode)
            .address(address)
            .detailAddress(detailAddress)
            .deliveryMemo(deliveryMemo)
            .build();
    }

}
