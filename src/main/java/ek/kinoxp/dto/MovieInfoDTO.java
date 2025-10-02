package ek.kinoxp.dto;

public record MovieInfoDTO(Long movieId, String title, String category, int ageLimit, int durationMin,
                           int releaseYear, String actors, String description, String language) {}

