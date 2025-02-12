package com.nebarrow.weathertracker.http.advice;

import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.exception.UserAlreadyExistsException;
import com.nebarrow.weathertracker.exception.UserNotFoundException;
import com.nebarrow.weathertracker.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Order(1)
@ControllerAdvice
public class UserAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFound(UserNotFoundException e) {
        log.error("User not found {}", e.getMessage());
        return createModelAndView("sign-in", "Invalid login or password", new User());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExists(UserAlreadyExistsException e) {
        log.error("User already exists {}", e.getMessage());
        return createModelAndView("sign-up", "Account with this email already exists. You can <a href='/login'>sign in here</a>.", new RegistrationRequest("", "", ""));
    }

    private ModelAndView createModelAndView(String viewName, String errorMessage, Object modelObject) {
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("errorMessage", errorMessage);
        mav.addObject(modelObject instanceof User ? "user" : "registrationRequest", modelObject);
        return mav;
    }
}
