package com.emotionalcart.order.infra.payment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponse {

    private PaymentHeader header;

    private PaymentBody body;

    public static class PaymentHeader {

    }

    public static class PaymentBody {

    }

    public static PaymentResponse empty() {
        return new PaymentResponse();
    }

}
