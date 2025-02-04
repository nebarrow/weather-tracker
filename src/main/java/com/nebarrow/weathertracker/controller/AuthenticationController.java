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

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "sign-in-with-errors";
    }

    @PostMapping("/login")
    public String login(@CookieValue String sessionId,
                        @ModelAttribute @Valid User user,
                        BindingResult result,
                        Model model,
                        HttpServletResponse response) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "sign-in-with-errors";
        }
        if (sessionId != null && !sessionId.isEmpty()) {
            return "redirect:/";
        }
        var foundUser = userService.findByLogin(user.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        try {
            HashPasswordUtil.checkPassword(user.getPassword(), foundUser.password());
            sessionId = String.valueOf(sessionService.create(foundUser.id()));
            var cookie = CookieUtil.set(UUID.fromString(sessionId));
            response.addCookie(cookie);
            return "redirect:/";
        } catch (InvalidPasswordException e) {
            log.error("Invalid password for user: {} with password: {}", user.getLogin(), user.getPassword());
            throw new InvalidPasswordException("Invalid password");
        }
    }
}
