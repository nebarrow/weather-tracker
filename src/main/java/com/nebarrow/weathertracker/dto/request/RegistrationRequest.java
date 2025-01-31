package com.nebarrow.weathertracker.dto.request;

public record RegistrationRequest(String login, String password, String repeatPassword) {
}
