package de.supercode.superbnb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(long id, @NotBlank String username,@NotBlank @Email String email) {
}
