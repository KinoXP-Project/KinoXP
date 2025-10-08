package ek.kinoxp.dto;

import java.time.LocalDateTime;

public class SearchShowingDTO {
    private Long showId;
    private String movieTitle;
    private String category;
    private Long theaterId;          // ← Long (ikke Integer)
    private String theaterName;
    private LocalDateTime startTime;

    public SearchShowingDTO(Long showId,
                            String movieTitle,
                            String category,
                            Long theaterId,           // ← matcher JPQL’s Long
                            String theaterName,
                            LocalDateTime startTime) {
        this.showId = showId;
        this.movieTitle = movieTitle;
        this.category = category;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.startTime = startTime;
    }

    public Long getShowId() { return showId; }
    public String getMovieTitle() { return movieTitle; }
    public String getCategory() { return category; }
    public Long getTheaterId() { return theaterId; }
    public String getTheaterName() { return theaterName; }
    public LocalDateTime getStartTime() { return startTime; }
}
