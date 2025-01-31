package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.exception.PasswordAreDifferentException;
import com.nebarrow.weathertracker.exception.UserAlreadyExistsException;
import com.nebarrow.weathertracker.model.User;
import com.nebarrow.weathertracker.service.UserService;
import com.nebarrow.weathertracker.util.HidePasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class RegistrationController {

    @Autowired
    private UserService service;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "sign-up-with-errors";
    }

    @PostMapping("/registration")
    public String register(@Validated @ModelAttribute("user") User user, @RequestParam("repeatPassword") String repeatPassword) {
        if (!user.getPassword().equals(repeatPassword)) {
            log.error("Password should be identical {}, {}", user.getPassword(), repeatPassword);
            throw new PasswordAreDifferentException("Passwords are different");
        }
        try {
            service.create(new PostUser(user.getLogin(), HidePasswordUtil.hashPassword(user.getPassword())));
        } catch (UserAlreadyExistsException e) {
            log.error("User with login {} already exists", user.getLogin());
            throw new UserAlreadyExistsException("User already exists");
        }
        return "redirect:/index";
    }
}
