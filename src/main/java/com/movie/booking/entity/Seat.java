package com.movie.booking.entity;

import jakarta.persistence.*;

@Entity
@Table(name="seat")
public class Seat {
    @Id
    @GeneratedValue
    private Long id;
    private String seatNumber;
    private boolean booked;

    @ManyToOne
    private Show show;

    public Seat()
    {}

    public Seat(String seatNumber, boolean booked, Show show) {
        this.seatNumber = seatNumber;
        this.booked = booked;
        this.show = show;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}
