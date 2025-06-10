package com.movie.booking.util;

import com.movie.booking.entity.Seat;
import com.movie.booking.entity.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountCalculator {
    private final List<DiscountStrategy> strategies;

    @Autowired
    public DiscountCalculator(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public double calculate(Show show, List<Seat> seats) {
        double basePrice = seats.size() * 200.0;
        for (DiscountStrategy strategy : strategies) {
            basePrice = strategy.applyDiscount(show, seats, basePrice);
        }
        return basePrice;
    }
}
