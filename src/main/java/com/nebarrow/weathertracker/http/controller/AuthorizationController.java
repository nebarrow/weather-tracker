package com.nebarrow.weathertracker.http.controller;

import com.nebarrow.weathertracker.dto.request.PostUserRequest;
import com.nebarrow.weathertracker.service.AuthenticationService;
import com.nebarrow.weathertracker.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthorizationController {

    private static final String MAIN_PAGE = "redirect:/";
    private static final String SIGN_IN = "sign-in";
    private static final String SIGN_IN_REDIRECT = "redirect:/login";

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return redirectToSignInPage(new PostUserRequest("", ""), model);
    }

    @PostMapping("/login")
    public String login(@CookieValue(name = "sessionId", defaultValue = "") String sessionId,
                        @ModelAttribute @Valid PostUserRequest postUser,
                        BindingResult result,
                        Model model,
                        HttpServletResponse response) {
        model.addAttribute("postUser", postUser);

        
        if (result.hasErrors()) {
            return redirectToSignInPage(postUser, model);
        }

        if (!sessionId.isEmpty()) {
            return MAIN_PAGE;
        }

        var session = authenticationService.login(postUser);
        var cookie = CookieUtil.set(session);
        response.addCookie(cookie);
        return MAIN_PAGE;
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(name = "sessionId", defaultValue = "") String sessionId, HttpServletResponse response) {
        authenticationService.logout(sessionId, response);
        return SIGN_IN_REDIRECT;
    }

    private String redirectToSignInPage(PostUserRequest user, Model model) {
        model.addAttribute("postUser", user);
        return SIGN_IN;
    }
}
