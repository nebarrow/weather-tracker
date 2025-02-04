package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.exception.PasswordAreDifferentException;
import com.nebarrow.weathertracker.exception.UserAlreadyExistsException;
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

    @Autowired
    private UserService service;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", ""));
        return "sign-up-with-errors";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute @Valid RegistrationRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registrationRequest", request);
            return "sign-up-with-errors";
        }
        if (!request.password().equals(request.repeatPassword())) {
            log.error("Password should be identical {}, {}", request.password(), request.repeatPassword());
            throw new PasswordAreDifferentException("Passwords are different");
        }
        try {
            service.create(new PostUser(request.username(), HashPasswordUtil.hashPassword(request.password())));
        } catch (UserAlreadyExistsException e) {
            log.error("User with login {} already exists", request.username());
            throw new UserAlreadyExistsException("User already exists");
        }
        return "redirect:/";
    }
}
