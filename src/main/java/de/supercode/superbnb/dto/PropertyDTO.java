package de.supercode.superbnb.dto;

import de.supercode.superbnb.entities.Property;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PropertyDTO(
        long id,
        @NotBlank
        String adress,
        @NotBlank
        String city,
        @Positive
        double pricePerNight) {

}