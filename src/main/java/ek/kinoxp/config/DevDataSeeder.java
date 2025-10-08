// src/main/java/ek/kinoxp/config/DevDataSeeder.java
package ek.kinoxp.config;

import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import ek.kinoxp.repository.MovieRepository;
import ek.kinoxp.repository.ShowRepository;
import ek.kinoxp.repository.TheaterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;

    public DevDataSeeder(TheaterRepository theaterRepository, MovieRepository movieRepository, ShowRepository showRepository) {
        this.theaterRepository = theaterRepository;
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() == 0 && theaterRepository.count() == 0) {

            // --- Theater (brug setters i stedet for constructor)
            Theater t1 = new Theater();
            t1.setName("Theater 1");
            t1.setRowCount(20);
            t1.setSeatCount(12);
            t1 = theaterRepository.save(t1);

            // --- Movie (brug setters der matcher getters i MovieDetailDTO)
            Movie dune = new Movie();
            dune.setTitle("Dune: Part Two");
            dune.setCategory("Sci-Fi");
            dune.setAgeLimit(12);
            dune.setDurationMin(166);
            dune.setReleaseYear(2024);
            dune.setLanguage("English");
            dune = movieRepository.save(dune);

            // --- Show (match felt-navnet! vælg én af de to linjer)
            Show s = new Show();
            s.setMovie(dune);
            s.setTheater(t1);
            s.setStartTime(LocalDateTime.now().plusDays(1));

            showRepository.save(s);
        }
    }
}
