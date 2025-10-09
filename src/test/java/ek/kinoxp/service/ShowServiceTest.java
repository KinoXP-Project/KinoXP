// src/test/java/ek/kinoxp/service/ShowServiceTest.java.
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ShowServiceTest {

    private ShowRepository showRepository;
    private MovieRepository movieRepository;
    private TheaterRepository theaterRepository;
    private ShowService showService;

    @BeforeEach
    void setUp() {
        this.showRepository = mock(ShowRepository.class);
        this.movieRepository = mock(MovieRepository.class);
        this.theaterRepository = mock(TheaterRepository.class);
        // NB: matcher din ShowService-konstruktør med 3 repos
        this.showService = new ShowService(showRepository, movieRepository, theaterRepository);
    }

    // ---------- Hjælpere ----------
    private Movie movie(long id, String title, int minutes) {
        Movie m = new Movie();
        m.setMovieId(id);
        m.setTitle(title);
        m.setDurationMin(minutes);
        m.setCategory("Sci-Fi");
        m.setAgeLimit(13);
        m.setReleaseYear(2010);
        m.setActors("Leo");
        m.setDescription("desc");
        m.setLanguage("English");
        return m;
    }

    private Theater theater(long id, String name) {
        Theater t = new Theater();
        t.setTheaterId(id);
        t.setName(name);
        t.setRowCount(10);
        t.setSeatCount(100);
        return t;
    }

    // ---------- Allerede godkendt: getUpcoming() ----------
    @Test
    void getUpcomingShowASC() {
        // Arrange
        Movie movie   = movie(1L,"Inception",148);
        Theater t     = theater(1L,"Sal 1");
        Show s1 = new Show(10L, movie, t, LocalDateTime.now().plusHours(1));
        Show s2 = new Show(11L, movie, t, LocalDateTime.now().plusHours(2));

        when(showRepository.findByStartAtAfterOrderByStartAtAsc(any()))
                .thenReturn(List.of(s1, s2));

        // Act
        List<UpcomingShowingDTO> result = showService.getUpcoming();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("Inception");
        assertThat(result.get(0).theater()).isEqualTo("Sal 1");
        assertThat(result.get(0).showId()).isEqualTo(10L);
        assertThat(result.get(1).showId()).isEqualTo(11L);

        verify(showRepository, times(1)).findByStartAtAfterOrderByStartAtAsc(any());
        verifyNoMoreInteractions(showRepository);
        // (movieRepo & theaterRepo ikke brugt i denne metode – ingen verifikation nødvendig)
    }

    // ---------- getMovieDetails(id) ----------
    @Test
    void getMovieDetails_returnsDTO_whenMovieExists() {
        // Arrange
        Movie m = movie(1L, "Interstellar", 169);
        m.setCategory("Sci-Fi");
        m.setAgeLimit(10);
        m.setReleaseYear(2014);
        m.setActors("Matthew McConaughey");
        m.setDescription("Space");
        m.setLanguage("English");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(m));

        // Act
        MovieDetailDTO dto = showService.getMovieDetails(1L);

        // Assert
        assertThat(dto.movieId()).isEqualTo(1L);
        assertThat(dto.title()).isEqualTo("Interstellar");
        assertThat(dto.durationMin()).isEqualTo(169);
        assertThat(dto.category()).isEqualTo("Sci-Fi");
        assertThat(dto.actors()).contains("Matthew");
    }

    // ---------- createShowing(req) ----------
    @Test
    void createShowing_happyPath_createsAndReturnsResponseDTO() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        Movie m = movie(1L, "Inception", 148);
        Theater t = theater(2L, "Sal 2");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(m));
        when(theaterRepository.findById(2L)).thenReturn(Optional.of(t));
        // ingen overlap i ±3 timer
        when(showRepository.existsByTheaterAndStartAtBetween(eq(t), any(), any())).thenReturn(false);
        // simulér save -> få id tilbage
        when(showRepository.save(any(Show.class))).thenAnswer(inv -> {
            Show s = inv.getArgument(0);
            // Lav et nyt Show med et "genereret" id og samme data
            return new Show(
                    42L,                // simuleret genereret id
                    s.getMovie(),
                    s.getTheater(),
                    s.getStartAt()
            );
        });
        var req = new CreateShowingRequestDTO(1L, 2L, start);

        // Act
        ShowingResponseDTO res = showService.createShowing(req);

        // Assert
        assertThat(res.showId()).isEqualTo(42L);
        assertThat(res.movieId()).isEqualTo(1L);
        assertThat(res.movieTitle()).isEqualTo("Inception");
        assertThat(res.theaterId()).isEqualTo(2L);
        assertThat(res.theaterName()).isEqualTo("Sal 2");
        assertThat(res.startAt()).isEqualTo(start);

        verify(showRepository).existsByTheaterAndStartAtBetween(eq(t), any(), any());
        verify(showRepository).save(any(Show.class));
    }

    @Test
    void createShowing_throws404_whenMovieNotFound() {
        // Arrange
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());
        var req = new CreateShowingRequestDTO(99L, 2L, LocalDateTime.now().plusDays(1));

        // Act + Assert
        assertThatThrownBy(() -> showService.createShowing(req))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
        verify(showRepository, never()).save(any());
    }

    @Test
    void createShowing_throws404_whenTheaterNotFound() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie(1L,"X",120)));
        when(theaterRepository.findById(999L)).thenReturn(Optional.empty());
        var req = new CreateShowingRequestDTO(1L, 999L, LocalDateTime.now().plusDays(1));

        // Act + Assert
        assertThatThrownBy(() -> showService.createShowing(req))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
        verify(showRepository, never()).save(any());
    }

    @Test
    void createShowing_throws409_whenOverlapInSameTheater() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        Movie m = movie(1L,"Inception",148);
        Theater t = theater(2L,"Sal 2");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(m));
        when(theaterRepository.findById(2L)).thenReturn(Optional.of(t));
        when(showRepository.existsByTheaterAndStartAtBetween(eq(t), any(), any()))
                .thenReturn(true); // <- overlap!

        var req = new CreateShowingRequestDTO(1L, 2L, start);

        // Act + Assert
        assertThatThrownBy(() -> showService.createShowing(req))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("409"); // CONFLICT
        verify(showRepository, never()).save(any());
    }
}