package ek.kinoxp.dto;

import java.time.LocalDateTime;
//Når backend sender data tilbage om det oprettede show
public record ShowingResponseDTO( Long showId,
                                  Long movieId,
                                  String movieTitle,
                                  Long theaterId,
                                  String theaterName,
                                  LocalDateTime startAt) {}
