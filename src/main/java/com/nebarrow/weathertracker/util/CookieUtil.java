package com.nebarrow.weathertracker.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CookieUtil {

    public static Cookie set(UUID id) {
        Cookie cookie = new Cookie("sessionId", id.toString());
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    public static boolean findByName(HttpServletRequest request, String name) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static Cookie delete(UUID sessionId) {
        Cookie cookie = new Cookie("sessionId", sessionId.toString());
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
