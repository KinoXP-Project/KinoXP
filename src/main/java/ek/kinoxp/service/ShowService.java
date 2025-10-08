package ek.kinoxp.service;

import ek.kinoxp.dto.SearchShowingDTO;
import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.model.Show;
import ek.kinoxp.repository.MovieRepository;
import ek.kinoxp.repository.ShowRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;

public ShowService(ShowRepository showRepository, MovieRepository movieRepository) {
    this.showRepository = showRepository;
    this.movieRepository = movieRepository;

}public List<UpcomingShowingDTO> getUpcomingShows() {
        // 1. Hent alle shows, der starter efter nu
        var shows = showRepository.findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime.now());

        // 2. Lav en tom liste, som vi fylder med DTO'er
        List<UpcomingShowingDTO> result = new ArrayList<>();

        // 3. Gennemløb alle shows ét for ét
        for (Show s : shows) {
            // 4. Opret en DTO ud fra show + relationer (Movie + Theater)
            UpcomingShowingDTO dto = new UpcomingShowingDTO(
                    s.getShowId(),
                    s.getMovie().getMovieId(),
                    s.getMovie().getTitle(),
                    s.getMovie().getCategory(),
                    s.getMovie().getAgeLimit(),
                    s.getTheater().getName(),
                    s.getStartTime(),
                    s.getMovie().getActors()
            );

            // 5. Tilføj DTO til listen
            result.add(dto);
        }
        // 6. Returner listen til controlleren (bliver sendt som JSON)
        return result;
    }

public MovieDetailDTO getMovieDetails(Long movieId) {
    var movie = movieRepository.findById(movieId).orElseThrow(); // Hvis filmen intet id har smid da exception
    return new MovieDetailDTO(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getCategory(),
            movie.getAgeLimit(),
            movie.getDurationMin(),
            movie.getReleaseYear(),
            movie.getActors(),
            movie.getDescription(),
            movie.getLanguage()
    );
}

    public List<SearchShowingDTO> searchShows(
            String title,
            String category,
            LocalDate start,   // dato uden klokkeslæt
            LocalDate end,
            Integer theaterId,
            String theaterName
    ) {
        LocalDateTime startAt = (start != null) ? start.atStartOfDay() : null;
        LocalDateTime endAt   = (end   != null) ? end.atTime(LocalTime.MAX) : null;

        return showRepository.searchShows(title, category, startAt, endAt, theaterId, theaterName);
    }

}