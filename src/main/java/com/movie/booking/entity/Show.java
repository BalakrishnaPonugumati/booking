package com.movie.booking.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="movie_show")
public class Show {
    @Id
    @GeneratedValue
    private Long id;

    private String movieTitle;
    private String theatre;
    private LocalDate date;
    private LocalTime time;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<Seat> seats;

    public Show() {
    }

    public Show(String movieTitle, String theatre, LocalDate date1, LocalTime time1) {
        this.movieTitle = movieTitle;
        this.theatre = theatre;
        this.date= date1;
        this.time = time1;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getTheatre() {
        return theatre;
    }

    public void setTheatre(String theatre) {
        this.theatre = theatre;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
