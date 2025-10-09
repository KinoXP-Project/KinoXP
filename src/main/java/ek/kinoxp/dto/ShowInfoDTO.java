package ek.kinoxp.DTO;

import java.time.LocalDateTime;

public record ShowInfoDTO(Long showId,
                          String movieTitle,
                          String theaterName,
                          int rowCount,
                          int seatCount,
                          LocalDateTime startTime) {}
