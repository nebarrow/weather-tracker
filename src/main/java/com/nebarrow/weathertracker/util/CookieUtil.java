package com.nebarrow.weathertracker.util;

import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CookieUtil {

    public static Cookie set(UUID id) {
        Cookie cookie = new Cookie("session_id", id.toString());
        cookie.setMaxAge(30 * 60);
        return cookie;
    }
}
