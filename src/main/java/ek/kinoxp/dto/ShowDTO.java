package ek.kinoxp.dto;

import ek.kinoxp.model.Show;

import java.time.LocalDateTime;

public record ShowDTO(Long showId,
                      Long movieId,
                      String movieTitle,
                      String category,
                      Integer ageLimit,
                      Long theaterId,
                      String theaterName,
                      LocalDateTime startTime,
                      LocalDateTime endTime
)
{
    public static ShowDTO from(Show s) {
        return new ShowDTO(
                s.getShowId(),
                s.getMovie().getMovieId(),
                s.getMovie().getTitle(),
                s.getMovie().getCategory(),
                s.getMovie().getAgeLimit(),
                s.getTheater().getTheaterId(),
                s.getTheater().getName(),
                s.getStartTime(),
                s.getEndTime()
        );
    }
}
