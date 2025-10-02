package ek.kinoxp.model;

import java.time.LocalDateTime;

public class Show {
    private Long showId;
    private Long movieId;    // FK -> Movie
    private Long theaterId;  // FK -> Theater
    private LocalDateTime startAt;

    public Show() {}

    public Show(Long showId, Long movieId, Long theaterId, LocalDateTime startAt) {
        this.showId = showId;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.startAt = startAt;
    }

    public Long getShowId() {
        return showId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }
    }
