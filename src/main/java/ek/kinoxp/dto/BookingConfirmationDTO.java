package ek.kinoxp.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record BookingConfirmationDTO(Long bookingId,
                                     LocalDateTime bookingTime,
                                     Long showId,
                                     String movieTitle,
                                     String theaterName,
                                     LocalDateTime startTime,
                                     List<SeatDTO> seats) {}
