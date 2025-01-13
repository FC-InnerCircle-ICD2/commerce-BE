package com.emotionalcart.order.infra.payment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponse {

    private PaymentHeader header;

    private PaymentBody body;

    public static class PaymentHeader {

    }

    public static class PaymentBody {

        private String paymentId;

    }

    public String getPaymentId() {
        return body.paymentId;
    }

    public static PaymentResponse empty() {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.body.paymentId = UUID.randomUUID().toString();
        return paymentResponse;
    }

}
