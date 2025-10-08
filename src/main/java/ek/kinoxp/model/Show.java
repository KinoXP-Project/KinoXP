package ek.kinoxp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shows")
public class Show
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    @ManyToOne(optional = false) //Én film → mange shows
    @JoinColumn(name = "movie_id") // fremmednøgle i shows-tabellen
    private Movie movie;

    @ManyToOne(optional = false) //Ét theater kan gøre → mange shows
    @JoinColumn(name = "theater_id") //fremmednøgle i shows-tabellen
    private Theater theater;

    @ManyToOne(optional = false)
    @JoinColumn(name = "program_id")
    private Program program;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Show(){}

    public Show(Movie movie, Theater theater, Program program, LocalDateTime startTime) {
        this.movie = movie;
        this.theater = theater;
        this.program = program;
        this.startTime = startTime;
        calculateEndTime();
    }

    //Getters og setters

    public void setShowId(Long showId) {this.showId = showId;}
    public Long getShowId() {return showId;}

    public Movie getMovie() {return movie;}
    public void setMovie(Movie movie) {this.movie = movie;}

    public Theater getTheater() {return theater;}
    public void setTheater(Theater theater) {this.theater = theater;}

    public Program getProgram() {return program;}
    public void setProgram(Program program) {this.program = program;}

    public LocalDateTime getStartTime() {return startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime() {return endTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}


    // Metode til at udregne filmens runtime, så vi kan udregne cirka sluttid
    public void calculateEndTime() {
        if (movie != null && startTime != null) {
            this.endTime = startTime.plusMinutes(movie.getRunTime());
        }
    }

}
