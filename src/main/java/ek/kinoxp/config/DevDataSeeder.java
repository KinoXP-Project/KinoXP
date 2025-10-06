// src/main/java/ek/kinoxp/config/DevDataSeeder.java
package ek.kinoxp.config;

import ek.kinoxp.Model.Movie;
import ek.kinoxp.Model.Show;
import ek.kinoxp.Model.Theater;
import ek.kinoxp.Repository.MovieRepository;
import ek.kinoxp.Repository.ShowRepository;
import ek.kinoxp.Repository.TheaterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final TheaterRepository theaterRepo;
    private final MovieRepository movieRepo;
    private final ShowRepository showRepo;

    public DevDataSeeder(TheaterRepository theaterRepo,
                         MovieRepository movieRepo,
                         ShowRepository showRepo) {
        this.theaterRepo = theaterRepo;
        this.movieRepo = movieRepo;
        this.showRepo = showRepo;
    }

    @Override
    public void run(String... args) {
        if (movieRepo.count() == 0 && theaterRepo.count() == 0) {

            // --- Theater (brug setters i stedet for constructor)
            Theater t1 = new Theater();
            t1.setName("Theater 1");
            t1.setRow_count(20);
            t1.setSeat_count(12);
            t1 = theaterRepo.save(t1);

            // --- Movie (brug setters der matcher dine getters i MovieDetailDTO)
            Movie dune = new Movie();
            dune.setTitle("Dune: Part Two");
            dune.setCategory("Sci-Fi");
            dune.setAgeLimit(12);
            dune.setDurationMin(166);
            dune.setReleaseYear(2024);
            dune.setLanguage("English");
            dune = movieRepo.save(dune);

            // --- Show (match felt-navnet! vælg én af de to linjer)
            Show s = new Show();
            s.setMovie(dune);
            s.setTheater(t1);
            // Hvis feltet hedder 'startAt':
            // s.setStartAt(LocalDateTime.now().plusDays(1));
            // Hvis feltet hedder 'startTime' (meget sandsynligt hos dig):
            s.setStartTime(LocalDateTime.now().plusDays(1));

            showRepo.save(s);
        }
    }
}
