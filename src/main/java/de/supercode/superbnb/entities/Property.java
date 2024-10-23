package de.supercode.superbnb.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Property {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adress;
    private String city;
    private double pricePerNight;
    private boolean availability;
    @OneToMany(mappedBy = "property")
    private List<Booking> bookings;
    public Property(Long id, String adress, String city, double pricePerNight, boolean availability, List<Booking> bookings) {
        this.id = id;
        this.adress = adress;
        this.city = city;
        this.pricePerNight = pricePerNight;
        this.availability = availability;
        this.bookings = bookings;
    }
    public Property(){

    }

    public Property(Long id, String adress, String city, double pricePerNight, boolean availability) {
        this(id, adress, city, pricePerNight, availability, new ArrayList<>()); // Инициализация bookings пустым списком
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
