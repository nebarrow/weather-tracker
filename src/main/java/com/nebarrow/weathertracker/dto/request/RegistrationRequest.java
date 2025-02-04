package com.nebarrow.weathertracker.dto.request;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record RegistrationRequest(
        @Email(message = "The entered value does not match the email format")
        String username,
        @Length(min = 5, message = "The password must be at least 5 characters long")
        String password,
        String repeatPassword) {
}
