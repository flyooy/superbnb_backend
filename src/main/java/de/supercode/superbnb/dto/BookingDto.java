package de.supercode.superbnb.dto;

import java.time.LocalDate;

public record BookingDto(
        LocalDate bookingDate,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
