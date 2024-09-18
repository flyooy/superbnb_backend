package de.supercode.superbnb.dto;

import java.time.LocalDate;

public record BookingProperty(
        LocalDate bookingDate,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
}
