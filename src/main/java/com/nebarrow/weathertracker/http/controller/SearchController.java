package com.nebarrow.weathertracker.http.controller;

import com.nebarrow.weathertracker.dto.request.LocationRequest;
import com.nebarrow.weathertracker.dto.response.GetUserResponse;
import com.nebarrow.weathertracker.dto.response.SessionResponse;
import com.nebarrow.weathertracker.dto.response.WeatherByNameResponse;
import com.nebarrow.weathertracker.model.entity.Session;
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

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private static final String SEARCH_PAGE = "search-results";
    private static final String MAIN_PAGE = "redirect:/";
    private final UserService userService;
    private final LocationService locationService;
    private final SessionService sessionService;

    @GetMapping("/search")
    public String showSearchPage(@CookieValue(name = "sessionId") String sessionId,
                                 @RequestParam("name") String name,
                                 Model model) {
        Session session = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(session.getUserId());
        var weather = locationService.getWeatherByName(name, user.id());
        addAttributesToModel(model, user, weather);
        return SEARCH_PAGE;
    }

    @PostMapping("/search")
    public String search(@CookieValue(name = "sessionId") String sessionId,
                         @RequestParam(value = "longitude", required = false) String longitude,
                         @RequestParam(value = "latitude", required = false) String latitude,
                         @RequestParam(value = "name", required = false) String name,
                         Model model) {
        Session session = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(session.getUserId());
        var weather = locationService.getWeatherByName(name, user.id());
        locationService.save(new LocationRequest(name, user.id(), Double.parseDouble(latitude), Double.parseDouble(longitude)));
        addAttributesToModel(model, user, weather);
        return MAIN_PAGE;
    }

    private void addAttributesToModel(Model model, GetUserResponse user, List<WeatherByNameResponse> weather) {
        model.addAttribute("user", user);
        model.addAttribute("weather", weather);
    }
}
