package ek.kinoxp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "show")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id")
    private Long showId;

    // Many shows = one movie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false) //
    private Movie movie;

    //  Many shows = one theater
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    public Show() {}

    public Show(Long showId, Movie movie, Theater theater, LocalDateTime startAt) {
        this.showId = showId;
        this.movie = movie;
        this.theater = theater;
        this.startAt = startAt;
    }

    // getters/setters
    public Long getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public Theater getTheater() { return theater; }
    public void setTheater(Theater theater) { this.theater = theater; }
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
}
