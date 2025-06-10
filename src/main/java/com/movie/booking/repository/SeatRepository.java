package com.movie.booking.repository;

import com.movie.booking.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowIdAndSeatNumberIn(Long showId, List<String> seatNumbers);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.show.id = :showId AND s.seatNumber IN :seatNumbers")
    List<Seat> findByShowIdAndSeatNumberInForUpdate(@Param("showId") Long showId, @Param("seatNumbers") List<String> seatNumbers);
}
