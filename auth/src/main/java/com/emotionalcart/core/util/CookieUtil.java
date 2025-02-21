package com.emotionalcart.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("emmotional-cart.click");
        cookie.setHttpOnly(false);  // JavaScript에서 접근 가능
        cookie.setSecure(true);   // HTTPS에서만 전송
        cookie.setPath("/");       // 모든 경로에서 쿠키 전송
        cookie.setMaxAge(maxAge);  // 쿠키 유효 시간 설정 (초 단위)
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }

}
