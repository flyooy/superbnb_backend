package de.supercode.superbnb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import java.time.LocalDate;

public record BookingCreateDto(
        @NotNull LocalDate bookingDate,
        @NotNull LocalDate checkInDate,
        @NotNull
        @Future(message = "Check-out date must be in the future")
        LocalDate checkOutDate,
        @NotNull Long propertyId,
        @NotNull Long userId
) {
}
