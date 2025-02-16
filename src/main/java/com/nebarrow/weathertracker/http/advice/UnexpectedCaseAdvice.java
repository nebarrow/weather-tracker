package com.nebarrow.weathertracker.http.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Order(4)
@ControllerAdvice
public class UnexpectedCaseAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnexpectedCase(RuntimeException e) {
        log.error("Unexpected case: {}", e.getMessage());
        return new ModelAndView("error");
    }
}
