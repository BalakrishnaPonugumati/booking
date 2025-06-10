package com.movie.booking.service;

import com.movie.booking.entity.Booking;
import com.movie.booking.entity.Seat;
import com.movie.booking.entity.Show;
import com.movie.booking.repository.BookingRepository;
import com.movie.booking.repository.SeatRepository;
import com.movie.booking.repository.ShowRepository;
import com.movie.booking.request.BookingRequest;
import com.movie.booking.util.DiscountCalculator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookingService {

    @Autowired
    private ShowRepository showRepo;
    @Autowired
    private SeatRepository seatRepo;
    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private DiscountCalculator discountCalculator;

    @Transactional
    public Booking bookSeats(BookingRequest request) {
        Long showId = request.getShowId();
        List<String> seatNumbers = request.getSeatNumbers();
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            throw new IllegalArgumentException("Seats must be selected");
        }

        Show show = showRepo.findById(showId).orElseThrow();
        List<Seat> seats = seatRepo.findByShowIdAndSeatNumberInForUpdate(showId, seatNumbers);
        if (seats.size() != seatNumbers.size()) throw new RuntimeException("Some seats not found");

        for (Seat seat : seats) {
            if (seat.isBooked()) throw new RuntimeException("Seat already booked: " + seat.getSeatNumber());
            seat.setBooked(true);
        }

        seatRepo.saveAll(seats);

        double price = discountCalculator.calculate(show, seats);

        Booking booking = new Booking();
        booking.setShow(show);
        booking.setSeatCount(seats.size());
        booking.setTotalPrice(price);
        booking.setCancelled(false);
        return bookingRepo.save(booking);

    }

    @Transactional
    public void cancelBooking(BookingRequest request) {
        Booking booking = bookingRepo.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.isCancelled()) throw new RuntimeException("Booking already cancelled");

        booking.setCancelled(true);
        bookingRepo.save(booking);

        List<Seat> seats = seatRepo.findByShowIdAndSeatNumberInForUpdate(
                booking.getShow().getId(), request.getSeatNumbers());
        for (Seat seat : seats) seat.setBooked(false);
        seatRepo.saveAll(seats);

    }
}
