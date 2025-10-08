package ek.kinoxp.service;

import ek.kinoxp.dto.CreateShowingRequestDTO;
import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.ShowingResponseDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import ek.kinoxp.repository.MovieRepository;
import ek.kinoxp.repository.ShowRepository;
import ek.kinoxp.repository.TheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    public ShowService(ShowRepository showRepository,
                       MovieRepository movieRepository,
                       TheaterRepository theaterRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    public List<UpcomingShowingDTO> getUpcoming() {
        List<Show> shows = showRepository.findByStartAtAfterOrderByStartAtAsc(LocalDateTime.now());
        List<UpcomingShowingDTO> result = new ArrayList<>();
        for (Show s : shows) {
            result.add(new UpcomingShowingDTO(
                    s.getShowId(),
                    s.getMovie().getMovieId(),
                    s.getMovie().getTitle(),
                    s.getMovie().getCategory(),
                    s.getMovie().getAgeLimit(),
                    s.getTheater().getName(),
                    s.getStartAt(),
                    s.getMovie().getActors()
            ));
        }
        return result;
    }

    public ShowingResponseDTO createShowing(CreateShowingRequestDTO req) {
        if (req == null || req.startAt() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "startAt is required");
        }

        Movie movie = movieRepository.findById(req.movieId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Movie not found: " + req.movieId()));

        Theater theater = theaterRepository.findById(req.theaterId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Theater not found: " + req.theaterId()));

        LocalDateTime newStart = req.startAt();
        if (!newStart.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Start time must be in the future");
        }

        // Grov guard: “noget” indenfor +/- 3 timer
        if (showRepository.existsByTheaterAndStartAtBetween(theater, newStart.minusHours(3), newStart.plusHours(3))) {
            throw new ResponseStatusException(CONFLICT, "Time slot unavailable in this theater.");
        }

        // Præcist overlap (samme dag) baseret på varighed
        LocalDateTime newEnd   = newStart.plusMinutes(movie.getDurationMin());
        LocalDateTime dayStart = newStart.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd   = newStart.toLocalDate().atTime(23, 59, 59);

        List<Show> sameDay = showRepository.findByTheater_TheaterIdAndStartAtBetween(
                theater.getTheaterId(), dayStart, dayEnd);

        for (Show s : sameDay) {
            LocalDateTime sStart = s.getStartAt();
            LocalDateTime sEnd   = sStart.plusMinutes(s.getMovie().getDurationMin());
            boolean overlaps = newStart.isBefore(sEnd) && newEnd.isAfter(sStart);
            if (overlaps) {
                throw new ResponseStatusException(CONFLICT, "Time slot overlaps with another show.");
            }
        }

        Show show = new Show();
        show.setMovie(movie);
        show.setTheater(theater);
        show.setStartAt(newStart);

        Show saved = showRepository.save(show);

        return new ShowingResponseDTO(
                saved.getShowId(),
                movie.getMovieId(),
                movie.getTitle(),
                theater.getTheaterId(),
                theater.getName(),
                saved.getStartAt()
        );
    }

    public MovieDetailDTO getMovieDetails(Long movieId) {
        var m = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Movie not found: " + movieId));
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
    }
}
