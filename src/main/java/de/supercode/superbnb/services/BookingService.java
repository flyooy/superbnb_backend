package de.supercode.superbnb.services;


import de.supercode.superbnb.dto.BookingDto;
import de.supercode.superbnb.dto.PropertyDTO;
import de.supercode.superbnb.entities.Booking;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    public List<BookingDto> getAllBookings(){

        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(booking -> new BookingDto(booking.getBookingDate(), booking.getCheckInDate(), booking.getCheckOutDate()))
                .collect(Collectors.toList());
    }

    public Booking createBooking(Booking booking){
        return bookingRepository.save(booking);
    }

    public void deleteBooking(long id){
        bookingRepository.deleteById(id);
    }
}
