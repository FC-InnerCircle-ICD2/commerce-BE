package com.emotionalcart.order.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

    /**
     * 금액
     */
    private BigDecimal amount;

    public double getAmount() {
        return amount.doubleValue();
    }

    /**
     * 파라미터로 전달된 수량과 합을 곱한뒤 전부 더해서 반환한다.
     *
     * @return 금액
     */
    public static Money sum(List<PriceAndQuantity> priceAndQuantities) {
        BigDecimal sum = priceAndQuantities.stream()
            .map(orderItem -> BigDecimal.valueOf(orderItem.getPrice()).multiply(BigDecimal.valueOf(orderItem.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        Money money = new Money();
        money.amount = sum;
        return money;
    }

    public static Money of(double price) {
        return new Money(price);
    }

    private Money(double price) {
        this.amount = BigDecimal.valueOf(price);
    }

}
