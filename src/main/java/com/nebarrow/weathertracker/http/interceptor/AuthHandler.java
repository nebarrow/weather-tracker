package com.nebarrow.weathertracker.http.interceptor;


import com.nebarrow.weathertracker.exception.ExpiredSessionException;
import com.nebarrow.weathertracker.service.SessionService;
import com.nebarrow.weathertracker.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.UUID;

@Component
public class AuthHandler implements HandlerInterceptor {

    private final SessionService sessionService;

    @Autowired
    public AuthHandler(@Lazy SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentUrl = request.getRequestURI();
        if (currentUrl.equals(request.getContextPath() + "/login")) {
            return true;
        }

        var cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        var sessionId = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);


        if (sessionId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        var session = sessionService.findById(UUID.fromString(sessionId));
        if (session == null) {
            var cookie = CookieUtil.delete(UUID.fromString(sessionId));
            response.addCookie(cookie);
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (!sessionService.isActive(UUID.fromString(sessionId))) {
            sessionService.delete(UUID.fromString(sessionId));
            throw new ExpiredSessionException("Session " + sessionId + " is expired");
        }
        return true;
    }

}
