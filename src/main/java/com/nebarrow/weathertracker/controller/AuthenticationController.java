package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.exception.InvalidPasswordException;
import com.nebarrow.weathertracker.exception.UserNotFoundException;
import com.nebarrow.weathertracker.model.User;
import com.nebarrow.weathertracker.service.SessionService;
import com.nebarrow.weathertracker.service.UserService;
import com.nebarrow.weathertracker.util.CookieUtil;
import com.nebarrow.weathertracker.util.HashPasswordUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Slf4j
@Controller
public class AuthenticationController {

    private static final String MAIN_PAGE = "redirect:/";
    private static final String SIGN_IN = "sign-in-with-errors";
    private static final String LOGIN = "redirect:/login";
    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public AuthenticationController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return handleValidationErrors(new User(), model);
    }

    @PostMapping("/login")
    public String login(@CookieValue(name = "sessionId", defaultValue = "") String sessionId,
                        @ModelAttribute @Valid User user,
                        BindingResult result,
                        Model model,
                        HttpServletResponse response) {
        if (result.hasErrors()) {
            return handleValidationErrors(user, model);
        }
        if (sessionId != null && !sessionId.isEmpty()) {
            return MAIN_PAGE;
        }
        return authenticateUser(user, response);
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(name = "session_id", defaultValue = "") String sessionId, HttpServletResponse response) {
        if (sessionId != null && !sessionId.isEmpty()) {
            sessionService.delete(UUID.fromString(sessionId));
            var cookie = CookieUtil.delete(UUID.fromString(sessionId));
            response.addCookie(cookie);
        }
        return LOGIN;
    }

    private String handleValidationErrors(User user, Model model) {
        model.addAttribute("user", user);
        return SIGN_IN;
    }

    private String authenticateUser(User user, HttpServletResponse response) {
        var foundUser = userService.findByLogin(user.getLogin());
        HashPasswordUtil.checkPassword(user.getPassword(), foundUser.password());
        createSessionAndAddCookie(foundUser.id(), response);
        return MAIN_PAGE;
    }

    private void createSessionAndAddCookie(int userId, HttpServletResponse response) {
        String sessionId = String.valueOf(sessionService.create(userId));
        var cookie = CookieUtil.set(UUID.fromString(sessionId));
        response.addCookie(cookie);
    }
}
