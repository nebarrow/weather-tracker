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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFound(UserNotFoundException e) {
        log.error("User not found", e);
        ModelAndView mav = new ModelAndView("sign-in-with-errors");
        mav.addObject("errorMessage", "Invalid username or password");
        mav.addObject("user", new User());
        return mav;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ModelAndView handleInvalidPassword(InvalidPasswordException e) {
        log.error("Invalid password", e);
        ModelAndView mav = new ModelAndView("sign-in-with-errors");
        mav.addObject("errorMessage", "Invalid username or password");
        mav.addObject("user", new User());
        return mav;
    }

    @ExceptionHandler(PasswordAreDifferentException.class)
    public ModelAndView handlePasswordAreDifferent(PasswordAreDifferentException e) {
        log.error("Passwords are different", e);
        ModelAndView mav = new ModelAndView("sign-up-with-errors");
        mav.addObject("errorMessage", "Passwords are different");
        mav.addObject("registrationRequest", new RegistrationRequest("", "", ""));
        return mav;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExists(UserAlreadyExistsException e) {
        log.error("User already exists", e);
        ModelAndView mav = new ModelAndView("sign-up-with-errors");
        mav.addObject("errorMessage",
                "Account with this email already exists. You can <a href='/login'>sign in here</a>.");
        mav.addObject("registrationRequest", new RegistrationRequest("", "", ""));
        return mav;
    }
}
