package de.supercode.superbnb.controllers;

import de.supercode.superbnb.dto.BookingCreateDto;
import de.supercode.superbnb.dto.BookingDto;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.entities.Booking;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.services.BookingService;
import de.supercode.superbnb.services.PropertyService;
import de.supercode.superbnb.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {
    @Autowired
    BookingService bookingService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    UserService userService;

    //@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings(){
        List<BookingDto> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingCreateDto bookingDTO) {
        Property property = propertyService.getPropertyById(bookingDTO.propertyId());
        if (property == null) {
            throw new EntityNotFoundException("Property not found with id " + bookingDTO.propertyId());
        }

        AppUser user = userService.getUserById(bookingDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + bookingDTO.userId()));

        Booking booking = new Booking();
        booking.setBookingDate(bookingDTO.bookingDate());
        booking.setCheckInDate(bookingDTO.checkInDate());
        booking.setCheckOutDate(bookingDTO.checkOutDate());
        booking.setProperty(property);
        booking.setUser(user);

        Booking createdBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBooking (@PathVariable Long id){
        try {
            bookingService.deleteBooking(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
