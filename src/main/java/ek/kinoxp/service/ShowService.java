package ek.kinoxp.service;

import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.model.Show;
import ek.kinoxp.repository.MovieRepository;
import ek.kinoxp.repository.ShowRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;

public ShowService(ShowRepository showRepository, MovieRepository movieRepository) {
    this.showRepository = showRepository;
    this.movieRepository = movieRepository;

}public List<UpcomingShowingDTO> getUpcoming() {
        // 1. Hent alle shows, der starter efter nu
        List<Show> shows = showRepository.findByStartAtAfterOrderByStartAtAsc(LocalDateTime.now());

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
                    s.getStartAt(),
                    s.getMovie().getActors()
            );

            // 5. Tilføj DTO til listen
            result.add(dto);
        }

        // 6. Returner listen til controlleren (bliver sendt som JSON)
        return result;
    }

public MovieDetailDTO getMovieDetails(Long movieId) {
    var m = movieRepository.findById(movieId).orElseThrow(); //throw =hvis film ingen id har smid exception
// Movie m = movieRepository.findById(movieId).orElseThrow();
    return new MovieDetailDTO(
            m.getMovieId(),
            m.getTitle(),
            m.getCategory(),
            m.getAgeLimit(),
            m.getDurationMin(),
            m.getReleaseYear(),
            m.getActors(),
            m.getDescription(),
            m.getLanguage()
    );
}}