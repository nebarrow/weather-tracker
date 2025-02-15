package com.nebarrow.weathertracker.exception;

import com.nebarrow.weathertracker.dto.request.RegistrationRequest;
import lombok.Getter;

@Getter
public class PasswordAreDifferentException extends RuntimeException {
    private final RegistrationRequest request;

    public PasswordAreDifferentException(String message, RegistrationRequest request) {
        super(message);
        this.request = request;
    }
}
