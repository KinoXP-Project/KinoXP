package ek.kinoxp.dto;

import java.time.LocalDateTime;

public record CreateShowingRequestDTO(Long movieId, Long theaterId, LocalDateTime startAt)   {}
