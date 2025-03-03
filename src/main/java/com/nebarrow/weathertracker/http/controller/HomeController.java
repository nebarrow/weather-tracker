package com.nebarrow.weathertracker.http.controller;

import com.nebarrow.weathertracker.dto.request.DeleteLocationRequest;
import com.nebarrow.weathertracker.dto.response.SessionResponse;
import com.nebarrow.weathertracker.model.entity.Session;
import com.nebarrow.weathertracker.service.LocationService;
import com.nebarrow.weathertracker.service.SessionService;
import com.nebarrow.weathertracker.service.UserService;
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
        Session session = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(session.getUserId());
        var userWeathers = locationService.getLocationWeatherByUserId(user.id());
        model.addAttribute("user", user);
        model.addAttribute("weathers", userWeathers);
        return MAIN_PAGE;
    }

    @PostMapping("/delete")
    public String deleteLocation(@CookieValue(name = "sessionId") String sessionId,
                                 @RequestParam("latitude") String latitude,
                                 @RequestParam("longitude") String longitude) {
        Session session = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(session.getUserId());
        locationService.delete(new DeleteLocationRequest(user.id(), Double.parseDouble(latitude), Double.parseDouble(longitude)));
        return MAIN_PAGE_REDIRECT;
    }
}
