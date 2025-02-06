package com.emotionalcart.order.infra.payment;

import lombok.Getter;

@Getter
public class PaymentInfo {

    private PaymentHeader header;

    private PaymentBody body;

    public static PaymentInfo create() {
        return null;
    }

    public static class PaymentHeader {
        // TODO 헤더 정보 추가
    }

    public static class PaymentBody {
        // TODO 바디 정보 추가
    }

}
