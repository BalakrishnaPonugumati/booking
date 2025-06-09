package com.movie.booking.repository;

import com.movie.booking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowIdAndSeatNumberIn(Long showId, List<String> seatNumbers);
}
