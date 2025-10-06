package ek.kinoxp.DTO;

import ek.kinoxp.Model.Show;

import java.time.LocalDateTime;

public record ShowDTO(Long id,
                      String movieTitle,
                      String category,
                      Integer ageLimit,
                      String theaterName,
                      LocalDateTime startTime,
                      LocalDateTime endTime
)
{
    public static ShowDTO from(Show s) {
        return new ShowDTO(
                s.getShow_id(),
                s.getProgram().getMovie().getTitle(),
                s.getProgram().getMovie().getCategory(),
                s.getProgram().getMovie().getAgeLimit(),
                s.getProgram().getTheater().getName(),
                s.getStartTime(),
                s.getEndTime()
        );
    }
}
