package Controller;

import Model.Movie;
import Repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController
{
    private MovieRepository repo;

    public MovieController(MovieRepository repo)
    {
        this.repo = repo;
    }

    //Get
    @GetMapping
    public List<Movie> getAllMovies(){
        return repo.findAll();
    }

}
