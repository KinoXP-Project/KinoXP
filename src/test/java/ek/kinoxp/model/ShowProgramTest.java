package ek.kinoxp.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ShowProgramTest {

    @Test
    void testAddShowAndRemoveShow() {
        Program program = new Program(1L, LocalDate.now(), LocalDate.now().plusMonths(3));
        Movie movie = new Movie();
        movie.setDurationMin(128);
        Theater theater = new Theater();

        LocalDateTime start = LocalDateTime.of(2025, 10, 10, 15, 0);
        Show show = new Show(program, movie, theater, start);

        program.addShow(show);
        assertTrue(program.getShows().contains(show));
        assertEquals(program, show.getProgram());

        program.removeShow(show);
        assertFalse(program.getShows().contains(show));
        assertNull(show.getProgram());
    }

    @Test
    void testCalculateEndTime() {
        Movie movie = new Movie();
        movie.setDurationMin(90);
        LocalDateTime start = LocalDateTime.of(2025, 10, 10, 14, 0);

        Show show = new Show();
        show.setMovie(movie);
        show.setStartTime(start);

        show.calculateEndTime();

        assertEquals(start.plusMinutes(90), show.getEndTime());
    }
}
