package com.emotionalcart.order.infra.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {

    /**
     * <h2>숫자가 아닌 문자열인지 확인</h2>
     * 숫자가 아닌 문자열인지 확인한다.
     *
     * @param str 확인할 문자열
     * @return 숫자가 아닌 문자열이면 true, 숫자이면 false
     */
    public static boolean isNotNumeric(String str) {
        if (str.trim().isEmpty()) {
            return true;
        }
        return !str.matches("-?\\d+(\\.\\d+)?");
    }

}
