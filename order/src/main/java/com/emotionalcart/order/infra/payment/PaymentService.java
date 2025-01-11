package com.emotionalcart.order.infra.payment;

import org.springframework.stereotype.Service;

/**
 * 결제 서비스
 */
@Service
public class PaymentService {

    /**
     * 결제
     */
    public PaymentResponse pay(PaymentInfo paymentInfo) {
        return PaymentResponse.empty();
    }

    /**
     * 결제 취소
     */
    public void cancel() {

    }

    /**
     * 결제 완료
     */
    public void complete() {

    }

    /**
     * 결제 실패
     */
    public void fail() {

    }

    /**
     * 결제 환불
     */
    public void refund() {

    }

    /**
     * 결제 환불 완료
     */
    public void refundComplete() {

    }

    /**
     * 결제 환불 실패
     */
    public void refundFail() {

    }

    /**
     * 결제 취소 완료
     */
    public void cancelComplete() {

    }

    /**
     * 결제 취소 실패
     */
    public void cancelFail() {

    }

    /**
     * 결제 취소 환불
     */
    public void cancelRefund() {

    }

    /**
     * 결제 취소 환불 완료
     */
    public void cancelRefundComplete() {

    }

    /**
     * 결제 취소 환불 실패
     */
    public void cancelRefundFail() {

    }

    /**
     * 결제 취소 환불 취소
     */
    public void cancelRefundCancel() {

    }

    /**
     * 결제 취소 환불 취소 완료
     */
    public void cancelRefundCancelComplete() {

    }

    /**
     * 결제 취소 환불 취소 실패
     */
    public void cancelRefundCancelFail() {

    }

    /**
     * 결제 취소 환불 취소 환불
     */
    public void cancelRefundCancelRefund() {

    }

    /**
     * 결제 취소 환불 취소 환불 완료
     */
    public void cancelRefundCancelRefundComplete() {

    }

}
