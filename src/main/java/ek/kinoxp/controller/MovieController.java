package ek.kinoxp.controller;

import ek.kinoxp.dto.CreateMovieDTO;
import ek.kinoxp.model.Movie;
import ek.kinoxp.repository.MovieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5500","http://127.0.0.1:5500"})
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // GET /api/movies
    @GetMapping
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    // POST /api/movies
    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody CreateMovieDTO dto) {
        var m = new Movie();
        m.setTitle(dto.title());
        m.setCategory(dto.category());
        m.setAgeLimit(dto.ageLimit());
        m.setDurationMin(dto.durationMin());
        m.setReleaseYear(dto.releaseYear());
        m.setActors(dto.actors());
        m.setDescription(dto.description());
        m.setLanguage(dto.language());
        var saved = movieRepository.save(m);
        return ResponseEntity.status(201).body(saved);
    }

}
