package com.nebarrow.weathertracker.http.controller;

import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private static final String SIGN_UP = "sign-up";
    private static final String MAIN_PAGE = "redirect:/";
    private static final String LOGIN_PAGE = "redirect:/login";
    private static final String SUCCESS_REGISTER_MESSAGE = "You have successfully registered. Please Sign-In";

    private final AuthenticationService authenticationService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", ""));
        return SIGN_UP;
    }

    @PostMapping("/registration")
    public String register(@CookieValue(name = "sessionId", defaultValue = "") String sessionId,
                           @ModelAttribute @Valid RegistrationRequest request,
                           BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return redirectToSignUpPage(request, model);
        }
        if (!sessionId.isEmpty()) {
            return MAIN_PAGE;
        }
        authenticationService.register(request);
        redirectAttributes.addFlashAttribute("successMessage", SUCCESS_REGISTER_MESSAGE);
        return LOGIN_PAGE;
    }

    private String redirectToSignUpPage(RegistrationRequest request, Model model) {
        model.addAttribute("registrationRequest", request);
        return SIGN_UP;
    }
}
