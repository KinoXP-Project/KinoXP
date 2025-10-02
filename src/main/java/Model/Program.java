package Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Program")
public class Program
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int program_id;

    @ManyToOne(optional = false) //Én film → mange shows
    @JoinColumn(name = "movie_id") // fremmednøgle i shows-tabellen
    private Movie movie;

    @ManyToOne(optional = false) //Én film → mange shows
    @JoinColumn(name = "theater_id") // fremmednøgle i shows-tabellen
    private Theater theater;

    private LocalDate startDate;
    private LocalDate endDate;

    public Program(){}

    public Program(int program_id, Movie movie, Theater theater, LocalDate startDate, LocalDate endDate)
    {
        this.program_id = program_id;
        this.movie = movie;
        this.theater = theater;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    //Getters og setters
    public int getProgram_id() {return program_id;}
    public void setProgram_id(int program_id) {this.program_id = program_id;}

    public Movie getMovie() {return movie;}
    public void setMovie(Movie movie) {this.movie = movie;}

    public Theater getTheater() {return theater;}
    public void setTheater(Theater theater) {this.theater = theater;}

    public LocalDate getStartDate() {return startDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}

    public LocalDate getEndDate() {return endDate;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

}
