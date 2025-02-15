package com.nebarrow.weathertracker.http.advice;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import com.nebarrow.weathertracker.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Order(2)
@ControllerAdvice
public class UserPasswordAdvice {

    @ExceptionHandler(InvalidPasswordException.class)
    public ModelAndView handleInvalidPassword(InvalidPasswordException e) {
        log.error("Invalid password {}", e.getMessage());
        return createModelAndView("sign-in", "Invalid login or password", new PostUser("", ""));
    }

    @ExceptionHandler(PasswordAreDifferentException.class)
    public ModelAndView handlePasswordAreDifferent(PasswordAreDifferentException e) {
        log.error("Passwords are different {}", e.getMessage());
        return createModelAndView("sign-up", "Passwords are different", e.getRequest());
    }

    private ModelAndView createModelAndView(String viewName, String errorMessage, Object modelObject) {
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("errorMessage", errorMessage);
        mav.addObject(modelObject instanceof PostUser ? "postUser" : "registrationRequest", modelObject);
        return mav;
    }
}
