package de.supercode.superbnb.services;


import de.supercode.superbnb.entities.Booking;
import de.supercode.superbnb.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking){
        return bookingRepository.save(booking);
    }

    public void deleteBooking(long id){
        bookingRepository.deleteById(id);
    }
}
