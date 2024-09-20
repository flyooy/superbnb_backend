package de.supercode.superbnb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthDto(
        @NotBlank(message = "Username is mandatory")  String username,
        @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Password is mandatory") String password
) {
}
