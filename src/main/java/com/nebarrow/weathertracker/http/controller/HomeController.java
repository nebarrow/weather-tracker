package com.nebarrow.weathertracker.http.controller;

import com.nebarrow.weathertracker.dto.request.LocationRequest;
import com.nebarrow.weathertracker.dto.response.SessionResponse;
import com.nebarrow.weathertracker.exception.LocationNotFoundException;
import com.nebarrow.weathertracker.service.LocationService;
import com.nebarrow.weathertracker.service.SessionService;
import com.nebarrow.weathertracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final String MAIN_PAGE = "home";
    private static final String MAIN_PAGE_REDIRECT = "redirect:/";
    private static final String ERROR_PAGE = "error";

    private final LocationService locationService;
    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping("/")
    public String showFindForm(@CookieValue(name = "sessionId") String sessionId, Model model) {
        SessionResponse sessionResponse = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(sessionResponse.userId());
        var userWeathers = locationService.getLocationWeatherByUserId(user.id());
        model.addAttribute("user", user);
        model.addAttribute("weathers", userWeathers);
        return MAIN_PAGE;
    }

    @PostMapping("/delete")
    public String deleteLocation(@CookieValue(name = "sessionId") String sessionId,
                                 @RequestParam("name") String name,
                                 @RequestParam("latitude") String latitude,
                                 @RequestParam("longitude") String longitude) {
        SessionResponse sessionResponse = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(sessionResponse.userId());
        try {
            locationService.delete(new LocationRequest(name, user.id(), Double.parseDouble(latitude), Double.parseDouble(longitude)));
        } catch (LocationNotFoundException e) {
            return ERROR_PAGE;
        }
        return MAIN_PAGE_REDIRECT;
    }
}
