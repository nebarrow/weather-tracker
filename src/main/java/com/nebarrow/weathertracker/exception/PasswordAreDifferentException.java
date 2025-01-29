package com.nebarrow.weathertracker.exception;

public class PasswordAreDifferentException extends RuntimeException {
    public PasswordAreDifferentException(String message) {
        super(message);
    }
}
