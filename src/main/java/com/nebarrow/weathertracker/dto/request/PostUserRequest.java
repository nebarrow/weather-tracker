package com.nebarrow.weathertracker.dto.request;

import jakarta.validation.constraints.Email;

public record PostUserRequest(
        @Email(message = "The entered value does not match the email format")
        String login,
        String password) {
}
