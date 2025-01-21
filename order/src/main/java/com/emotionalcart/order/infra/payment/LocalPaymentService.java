package com.emotionalcart.order.infra.payment;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 로컬 결제 서비스
 */
@Service
@Profile({"local", "default", "test"})
public class LocalPaymentService implements PaymentService {

    @Override
    public void pay(PaymentInfo paymentInfo) {
        System.out.println("LocalPaymentService.pay");
    }

    @Override
    public void cancel() {
        System.out.println("LocalPaymentService.cancel");
    }

    @Override
    public void refund() {
        System.out.println("LocalPaymentService.refund");
    }

    @Override
    public void cancelRefund() {
        System.out.println("LocalPaymentService.cancelRefund");
    }

    @Override
    public void cancelRefundCancel() {
        System.out.println("LocalPaymentService.cancelRefundCancel");
    }

}
