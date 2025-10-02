package DTO;

import Model.Show;

import java.time.LocalDateTime;

public record ShowDTO(int id,
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
                s.getProgram_id().getMovie().getTitle(),
                s.getProgram_id().getMovie().getCategory(),
                s.getProgram_id().getMovie().getAge_limit(),
                s.getProgram_id().getTheater().getName(),
                s.getStartTime(),
                s.getEndTime()
        );
    }
}
