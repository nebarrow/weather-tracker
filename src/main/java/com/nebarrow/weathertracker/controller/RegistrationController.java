package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.exception.PasswordAreDifferentException;
import com.nebarrow.weathertracker.service.UserService;
import com.nebarrow.weathertracker.util.HashPasswordUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class RegistrationController {

    private static final String SIGN_UP = "sign-up-with-errors";
    private static final String MAIN_PAGE = "redirect:/";

    private final UserService service;

    @Autowired
    public RegistrationController(UserService service) {
        this.service = service;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", ""));
        return SIGN_UP;
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute @Valid RegistrationRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return handleValidationErrors(request, model);
        }
        registrationUser(request);
        return MAIN_PAGE;
    }

    private String handleValidationErrors(RegistrationRequest request, Model model) {
        model.addAttribute("registrationRequest", request);
        return SIGN_UP;
    }

    private void registrationUser(RegistrationRequest request) {
        if (!request.password().equals(request.repeatPassword())) {
            log.error("Passwords are different: {}, {}", request.password(), request.repeatPassword());
            throw new PasswordAreDifferentException("Passwords are different");
        }
        service.create(new PostUser(request.username(), HashPasswordUtil.hashPassword(request.password())));
    }
}
