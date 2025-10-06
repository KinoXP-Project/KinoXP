package ek.kinoxp.Service;

import ek.kinoxp.DTO.MovieDetailDTO;
import ek.kinoxp.DTO.UpcomingShowingDTO;
import ek.kinoxp.Model.Show;
import ek.kinoxp.Repository.MovieRepository;
import ek.kinoxp.Repository.ShowRepository;

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
        var shows = showRepository.findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime.now());

        // 2. Lav en tom liste, som vi fylder med DTO'er
        List<UpcomingShowingDTO> result = new ArrayList<>();

        // 3. Gennemløb alle shows ét for ét
        for (Show s : shows) {
            // 4. Opret en DTO ud fra show + relationer (Movie + Theater)
            UpcomingShowingDTO dto = new UpcomingShowingDTO(
                    s.getShow_id(),
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