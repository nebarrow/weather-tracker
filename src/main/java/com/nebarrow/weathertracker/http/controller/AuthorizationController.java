package com.nebarrow.weathertracker.http.controller;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.service.AuthenticationService;
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
        return redirectToSignInPage(new PostUser("", ""), model);
    }

    @PostMapping("/login")
    public String login(@CookieValue(name = "sessionId", defaultValue = "") String sessionId,
                        @ModelAttribute @Valid PostUser postUser,
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

        authenticationService.login(postUser, response);
        return MAIN_PAGE;
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(name = "sessionId", defaultValue = "") String sessionId, HttpServletResponse response) {
        authenticationService.logout(sessionId, response);
        return SIGN_IN_REDIRECT;
    }

    private String redirectToSignInPage(PostUser user, Model model) {
        model.addAttribute("postUser", user);
        return SIGN_IN;
    }
}
