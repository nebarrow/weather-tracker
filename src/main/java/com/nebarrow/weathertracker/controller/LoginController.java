package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.exception.InvalidPasswordException;
import com.nebarrow.weathertracker.exception.UserNotFoundException;
import com.nebarrow.weathertracker.model.User;
import com.nebarrow.weathertracker.service.UserService;
import com.nebarrow.weathertracker.util.HidePasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "sign-in-with-errors";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        var foundUser = userService.findByLogin(user.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        try {
            HidePasswordUtil.checkPassword(user.getPassword(), foundUser.password());
            return "redirect:/find";
        } catch (InvalidPasswordException e) {
            throw new InvalidPasswordException("Invalid password");
        }
    }
}
