package com.emotionalcart.order.infra.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    /**
     * <h2>마지막 날짜 반환</h2>
     * 넘겨진 파라미터의 날짜로 마지막날을 반환<br/>
     * 넘기는 데이터의 형식은 MM/YY
     *
     * @return 마지막 날짜
     */
    public static LocalDate getLastDateFromExpirationDate(String date) {
        String[] dates = date.split("/");
        LocalDate expirationLocalDate = LocalDate.of(2000 + Integer.parseInt(dates[1]), Integer.parseInt(dates[0]), 1);
        return expirationLocalDate.with(TemporalAdjusters.lastDayOfMonth());
    }

}
