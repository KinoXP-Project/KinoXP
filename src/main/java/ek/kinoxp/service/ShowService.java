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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    public ShowService(ShowRepository showRepository, MovieRepository movieRepository, TheaterRepository theaterRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }

    public List<UpcomingShowingDTO> getUpcoming() {
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
    public ShowingResponseDTO createShowing(CreateShowingRequestDTO req) {
        Movie movie = movieRepository.findById(req.movieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + req.movieId()));
        Theater theater = theaterRepository.findById(req.theaterId())
                .orElseThrow(() -> new IllegalArgumentException("Theater not found: " + req.theaterId()));

        if (!req.startAt().isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("Start time must be in the future");

        var newStart = req.startAt();
        var newEnd   = newStart.plusMinutes(movie.getDurationMin());

        var dayStart = newStart.toLocalDate().atStartOfDay();
        var dayEnd   = newStart.toLocalDate().atTime(23,59,59);

        var sameDay = showRepository.findByTheater_TheaterIdAndStartAtBetween(
                theater.getTheaterId(), dayStart, dayEnd);

        for (Show s : sameDay) {
            var sStart = s.getStartAt();
            var sEnd   = sStart.plusMinutes(s.getMovie().getDurationMin());
            if (newStart.isBefore(sEnd) && newEnd.isAfter(sStart)) {
                throw new IllegalStateException("Time slot unavailable in this theater");
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

//
//    public ShowingResponseDTO createShowing(CreateShowingRequestDTO request) {
//        // 1) slå foreign keys op
//        Movie movie = movieRepository.findById(request.movieId())
//                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + request.movieId()));
//        Theater theater = theaterRepository.findById(request.theaterId())
//                .orElseThrow(() -> new IllegalArgumentException("Theater not found: " + request.theaterId()));
//
//        LocalDateTime start = request.startAt();
//
//        // 2) overlap-tjek (simpel: ingen andre shows i samme sal inden for ±3 timer)
//        boolean overlaps = showRepository.existsByTheaterAndStartAtBetween(
//                theater,
//                start.minusHours(3),
//                start.plusHours(3)
//        );
//        if (overlaps) {
//            throw new IllegalStateException("Time slot unavailable in this theater.");
//        }
//
//        // 3) opret og gem
//        Show s = new Show();
//        s.setMovie(movie);
//        s.setTheater(theater);
//        s.setStartAt(start);
//
//        Show saved = showRepository.save(s);
//
//        // 4) svar-DTO
//        return new ShowingResponseDTO(
//                saved.getShowId(),
//                movie.getMovieId(),
//                movie.getTitle(),
//                theater.getTheaterId(),
//                theater.getName(),
//                saved.getStartAt()
//        );
//    }


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