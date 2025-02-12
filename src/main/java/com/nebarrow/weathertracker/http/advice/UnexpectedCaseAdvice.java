package com.nebarrow.weathertracker.http.advice;

import com.nebarrow.weathertracker.exception.OpenWeatherApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class UnexpectedCaseAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnexpectedCase(RuntimeException e) {
        log.error("Unexpected case: {}", e.getMessage());
        return new ModelAndView("error");
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleOpenWeatherApiError(OpenWeatherApiException e) {
        log.error("OpenWeather API error: {}", e.getMessage());
        return new ModelAndView("error");
    }
}
