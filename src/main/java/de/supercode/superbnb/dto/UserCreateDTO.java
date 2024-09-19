package de.supercode.superbnb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public record UserCreateDTO(@NotBlank(message = "Username is required")
                            String username,

                            @NotBlank(message = "Email is required")
                            @Email(message = "Email should be valid")
                            String email,

                            @NotBlank(message = "Password is required")
                            @Size(min = 6, message = "Password must be at least 6 characters")
                            String password  ) {
}
