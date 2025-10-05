package ek.kinoxp.Service;

import ek.kinoxp.Model.Movie;
import ek.kinoxp.Model.Show;
import ek.kinoxp.Repository.MovieRepository;
import ek.kinoxp.Repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProgramServiceTest
{
    private MovieRepository movieRepository;
    private ShowRepository showRepository;
    private ProgramService programService;


    @BeforeEach
    void setUp(){
        this.movieRepository = mock(MovieRepository.class);
        this.showRepository = mock(ShowRepository.class);
        this.programService = new ProgramService(showRepository, movieRepository);
    }

    @Test
    void getProgram_Within3Months() {
        Movie movie = new Movie(1, "Inception", "Sci-Fi", 148, 13, 2010, "English");

        Show s1 = new Show();
        s1.setShow_id(1);
        s1.setMovie(1);
        s1.setProgram(1);
        s1.setTheater(1);
        s1.setStartTime(LocalDateTime.of(2025,10,14,20,0));
        s1.setEndTime(LocalDateTime.of(2025,10,14,23,0));
        s1.setMovie(movie);

        when(showRepository.findByStartTimeBetween(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(s1));

        var result = programService.getProgram(
                LocalDate.of(2025,10,1),
                LocalDate.of(2025,10,31));

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).movieTitle());
    }

}

