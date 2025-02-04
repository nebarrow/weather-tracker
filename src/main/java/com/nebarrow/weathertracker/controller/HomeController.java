package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.service.LocationService;
import com.nebarrow.weathertracker.service.SessionService;
import com.nebarrow.weathertracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showFindForm(@CookieValue(name = "session_id", defaultValue = "") String sessionId, Model model) {
        var session = sessionService.findById(UUID.fromString(sessionId));
        var locations = locationService.findByUserId(session.get().userId());
        var userLogin = userService.findById(session.get().userId()).get().login();
        model.addAttribute("locations", locations);
        model.addAttribute("userLogin", userLogin);
        return "index";
    }

    @PostMapping("/")
    public String find() {
        return "redirect:/";
    }
}
