package com.nebarrow.weathertracker.http.advice;

import com.nebarrow.weathertracker.exception.ExpiredSessionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Order(3)
@ControllerAdvice
public class SessionAdvice {
    @ExceptionHandler(ExpiredSessionException.class)
    public String handleExpiredSession(ExpiredSessionException e, RedirectAttributes redirectAttributes) throws IOException {
        log.error("Session is expired: {}", e.getMessage());
        return redirectToSignInPage(redirectAttributes);
    }

    private String redirectToSignInPage(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Your session has expired. Please sign in again");
        return "redirect:/login";
    }
}
