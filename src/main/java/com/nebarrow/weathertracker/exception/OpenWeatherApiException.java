package com.nebarrow.weathertracker.exception;

public class OpenWeatherApiException extends RuntimeException {
    public OpenWeatherApiException(String message) {
        super(message);
    }
}
