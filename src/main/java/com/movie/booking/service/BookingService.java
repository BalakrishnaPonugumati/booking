package com.movie.booking.service;

import com.movie.booking.entity.Booking;
import com.movie.booking.entity.Seat;
import com.movie.booking.entity.Show;
import com.movie.booking.repository.BookingRepository;
import com.movie.booking.repository.SeatRepository;
import com.movie.booking.repository.ShowRepository;
import com.movie.booking.request.BookingRequest;
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

    private final Map<Long, Object> showLocks = new ConcurrentHashMap<>();
    public Booking bookSeats(BookingRequest request) {
        Long showId = request.getShowId();
        List<String> seatNumbers = request.getSeatNumbers();
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            throw new IllegalArgumentException("Seats must be selected");
        }

        Object lock = showLocks.computeIfAbsent(showId, id -> new Object());
        synchronized (lock) {
            Show show = showRepo.findById(showId).orElseThrow();
            List<Seat> seats = seatRepo.findByShowIdAndSeatNumberIn(showId, seatNumbers);
            if (seats.size() != seatNumbers.size()) throw new RuntimeException("Some seats not found");

            for (Seat seat : seats) {
                if (seat.isBooked()) throw new RuntimeException("Seat already booked: " + seat.getSeatNumber());
                seat.setBooked(true);
            }

            seatRepo.saveAll(seats);

            double price = seats.size() * 200.0;
            if (seats.size() >= 3) price -= 100.0;
            if (show.getTime().isAfter(LocalTime.NOON) && show.getTime().isBefore(LocalTime.of(17, 0))) price *= 0.8;

            Booking booking = new Booking();
            booking.setShow(show);
            booking.setSeatCount(seats.size());
            booking.setTotalPrice(price);
            booking.setCancelled(false);
            return bookingRepo.save(booking);
        }
    }

    public void cancelBooking(BookingRequest request) {
        Booking booking = bookingRepo.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.isCancelled()) throw new RuntimeException("Booking already cancelled");

        Object lock = showLocks.computeIfAbsent(booking.getShow().getId(), id -> new Object());
        synchronized (lock) {
            booking.setCancelled(true);
            bookingRepo.save(booking);

            List<Seat> seats = seatRepo.findByShowIdAndSeatNumberIn(
                    booking.getShow().getId(), request.getSeatNumbers());
            for (Seat seat : seats) seat.setBooked(false);
            seatRepo.saveAll(seats);
        }
    }
}
