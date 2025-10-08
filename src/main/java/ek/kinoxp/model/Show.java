package ek.kinoxp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shows")
public class Show
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id")
    private Long showId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Show(){}

    public Show(Program program, Movie movie, Theater theater, LocalDateTime startTime) {
        this.program = program;
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        calculateEndTime();
    }

    //Getters og setters

    public void setShowId(Long showId) {this.showId = showId;}
    public Long getShowId() {return showId;}

    public Program getProgram() {return program;}
    public void setProgram(Program program) {this.program = program;}

    public Movie getMovie() {return movie;}
    public void setMovie(Movie movie) {this.movie = movie;}

    public Theater getTheater() {return theater;}
    public void setTheater(Theater theater) {this.theater = theater;}

    public LocalDateTime getStartTime() {return startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime() {return endTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    public void calculateEndTime() {
        if (movie != null && startTime != null) {
            this.endTime = startTime.plusMinutes(movie.getDurationMin());
        }
    }

}
