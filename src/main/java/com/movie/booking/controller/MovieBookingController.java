package com.movie.booking.controller;

import com.movie.booking.entity.Booking;
import com.movie.booking.entity.Show;
import com.movie.booking.repository.ShowRepository;
import com.movie.booking.request.BookingRequest;
import com.movie.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MovieBookingController {
    @Autowired
    private ShowRepository showRepo;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/shows")
    public List<Show> browseShows(@RequestParam String movieTitle, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return showRepo.findByMovieTitleAndDate(movieTitle, date);
    }

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.bookSeats(request);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancel(@RequestBody BookingRequest bookingRequest) {
        try {
            bookingService.cancelBooking(bookingRequest);
            return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
