package de.supercode.superbnb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PropertyCreateDTO(
        @NotBlank String adress,
        @NotBlank String city,
        @Positive double pricePerNight,
        boolean availability) {
}
