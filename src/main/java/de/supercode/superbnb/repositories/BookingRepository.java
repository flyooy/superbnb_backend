package de.supercode.superbnb.repositories;

import de.supercode.superbnb.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
