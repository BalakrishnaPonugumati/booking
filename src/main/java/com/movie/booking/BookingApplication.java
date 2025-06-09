package com.movie.booking;

import com.movie.booking.entity.Seat;
import com.movie.booking.entity.Show;
import com.movie.booking.repository.SeatRepository;
import com.movie.booking.repository.ShowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = "com.movie.booking.entity") // adjust as per your entity package
@EnableJpaRepositories(basePackages = "com.movie.booking.repository")
public class BookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}

	@Bean
	CommandLineRunner loadData(ShowRepository showRepo, SeatRepository seatRepo) {
		return args -> {
			Show show = new Show("Inception", "PVR Cinemas", LocalDate.now(), LocalTime.of(14, 0));
			showRepo.save(show);

			seatRepo.saveAll(List.of(
					new Seat("A1", false, show),
					new Seat("A2", false, show),
					new Seat("A3", false, show),
					new Seat("A4", false, show),
					new Seat("A5", false, show),
					new Seat("A6", false, show),
					new Seat("A7", false, show),
					new Seat("A8", false, show),
					new Seat("A9", false, show),
					new Seat("B1", false, show),
					new Seat("B2", false, show),
					new Seat("B3", false, show)
			));
		};
	}

}
