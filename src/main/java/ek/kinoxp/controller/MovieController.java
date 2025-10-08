package ek.kinoxp.controller;

import ek.kinoxp.model.Movie;
import ek.kinoxp.repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController
{
    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository)
    {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
}
