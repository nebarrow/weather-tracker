package com.nebarrow.weathertracker.interceptor;


import com.nebarrow.weathertracker.service.SessionService;
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
        var sessionId = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        if (sessionId == null || sessionService.findById(UUID.fromString(sessionId)).isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
