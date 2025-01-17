package com.emotionalcart.order.infra.payment;

/**
 * 결제 서비스
 */
public interface PaymentService {

    /**
     * 결제
     */
    void pay(PaymentInfo paymentInfo);

    /**
     * 결제 취소
     */
    void cancel();

    /**
     * 결제 환불
     */
    void refund();

    /**
     * 결제 취소 환불
     */
    void cancelRefund();

    /**
     * 결제 취소 환불 취소
     */
    void cancelRefundCancel();

}
