package com.movie.booking.util;

import com.movie.booking.entity.Seat;
import com.movie.booking.entity.Show;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class AfternoonShowDiscount implements DiscountStrategy{
    @Override
    public double applyDiscount(Show show, List<Seat> seats, double basePrice) {
        LocalTime time = show.getTime();
        if (time.isAfter(LocalTime.NOON) && time.isBefore(LocalTime.of(17, 0))) {
            return basePrice * 0.8;
        }
        return basePrice;
    }
}
