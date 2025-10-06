package ek.kinoxp.DTO;

import java.time.LocalDateTime;

public record UpcomingShowingDTO(Long showId, Long movieId, String title, String category,
        int ageLimit, String theater, LocalDateTime startAt, String actors) {}