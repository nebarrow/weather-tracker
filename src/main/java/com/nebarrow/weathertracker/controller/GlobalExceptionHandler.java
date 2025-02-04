package com.nebarrow.weathertracker.controller;

import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.exception.InvalidPasswordException;
import com.nebarrow.weathertracker.exception.PasswordAreDifferentException;
import com.nebarrow.weathertracker.exception.UserAlreadyExistsException;
import com.nebarrow.weathertracker.exception.UserNotFoundException;
import com.nebarrow.weathertracker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFound(UserNotFoundException e) {
        log.error("User not found {}", e.getMessage());
        return createModelAndView("sign-in-with-errors", "Invalid username or password", new User());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ModelAndView handleInvalidPassword(InvalidPasswordException e) {
        log.error("Invalid password {}", e.getMessage());
        return createModelAndView("sign-in-with-errors", "Invalid username or password", new User());
    }

    @ExceptionHandler(PasswordAreDifferentException.class)
    public ModelAndView handlePasswordAreDifferent(PasswordAreDifferentException e) {
        log.error("Passwords are different {}", e.getMessage());
        return createModelAndView("sign-up-with-errors", "Passwords are different", new RegistrationRequest("", "", ""));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExists(UserAlreadyExistsException e) {
        log.error("User already exists {}", e.getMessage());
        return createModelAndView("sign-up-with-errors", "Account with this email already exists. You can <a href='/login'>sign in here</a>.", new RegistrationRequest("", "", ""));
    }

    private ModelAndView createModelAndView(String viewName, String errorMessage, Object modelObject) {
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("errorMessage", errorMessage);
        mav.addObject(modelObject instanceof User ? "user" : "registrationRequest", modelObject);
        return mav;
    }
}
