package com.movie.booking.util;

import com.movie.booking.entity.Seat;
import com.movie.booking.entity.Show;

import java.util.List;

public interface DiscountStrategy {
    double applyDiscount(Show show, List<Seat> seats, double basePrice);
}
