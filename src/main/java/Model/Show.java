package Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Show")
public class Show
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int show_id;

    @ManyToOne(optional = false) //Én film → mange shows
    @JoinColumn(name = "movie_id") // fremmednøgle i shows-tabellen
    private Movie movie;

    @ManyToOne(optional = false) //Ét theater kan gøre → mange shows
    @JoinColumn(name = "theater_id") //fremmednøgle i shows-tabellen
    private Theater theater;

    private int program_id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Show(){}

    public Show(Movie movie, Theater theater, LocalDateTime startTime) {
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        calculateEndTime();
    }

    //Getters og setters

    public void setShow_id(int show_id) {this.show_id = show_id;}
    public int getShow_id() {return show_id;}

    public Movie getMovie() {return movie;}
    public void setMovie(Movie movie) {this.movie = movie;}

    public Theater getTheater() {return theater;}
    public void setTheater(Theater theater) {this.theater = theater;}

    public int getProgram_id() {return program_id;}
    public void setProgram_id(int program_id) {this.program_id = program_id;}

    public LocalDateTime getStartTime() {return startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime() {return endTime;}
    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}


    // Metode til at udregne filmens runtime, så vi kan udregne cirka sluttid

    public void calculateEndTime() {
        if (movie != null && startTime != null) {
            this.endTime = startTime.plusMinutes(movie.getRun_time());
        }
    }

}
