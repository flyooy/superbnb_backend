package de.supercode.superbnb.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInDto(
        @NotBlank(message = "Email is mandatory") String email,
        @NotBlank(message = "Password is mandatory") String password
) {
}
