package com.movie.booking.util;

import com.movie.booking.entity.Seat;
import com.movie.booking.entity.Show;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupBookingDiscount implements DiscountStrategy{
    @Override
    public double applyDiscount(Show show, List<Seat> seats, double basePrice) {
        return seats.size() >= 3 ? basePrice - 100.0 : basePrice;
    }
}
