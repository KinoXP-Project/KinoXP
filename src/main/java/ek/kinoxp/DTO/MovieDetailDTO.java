package ek.kinoxp.DTO;

public record MovieDetailDTO(Long movieId, String title, String category, int ageLimit, int durationMin,
                             int releaseYear, String actors, String description, String language) {

    }


