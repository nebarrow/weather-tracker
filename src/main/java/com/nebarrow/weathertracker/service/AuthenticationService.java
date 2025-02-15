package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.exception.PasswordAreDifferentException;
import com.nebarrow.weathertracker.util.CookieUtil;
import com.nebarrow.weathertracker.util.HashPasswordUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public UUID login(PostUser user) {
        var userWithSameLogin = userService.findByLogin(user.login());
        HashPasswordUtil.checkPassword(user.password(), userWithSameLogin.password());
        return sessionService.create(userWithSameLogin.id());
    }

    @Transactional
    public void register(RegistrationRequest user) {
        if (!user.password().equals(user.repeatPassword())) {
            log.error("Passwords are different: {}, {}", user.password(), user.repeatPassword());
            throw new PasswordAreDifferentException("Passwords are different");
        }
        userService.create(new PostUser(user.username(), HashPasswordUtil.hashPassword(user.password())));
    }

    @Transactional
    public void logout(String sessionId, HttpServletResponse response) {
        try {
            sessionService.delete(UUID.fromString(sessionId));
            var cookie = CookieUtil.delete(UUID.fromString(sessionId));
            response.addCookie(cookie);
        } catch (IllegalArgumentException e) {
            log.error("Session id is not valid: {}", sessionId);
            throw new RuntimeException("Session id is not valid");
        }
    }

}
