package ek.kinoxp.service;

import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import ek.kinoxp.repository.MovieRepository;
import ek.kinoxp.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShowServiceTest {
    private ShowRepository showRepository;
    private MovieRepository movieRepository;
    private ShowService showService;

    @BeforeEach
    void setUp() {
        this.showRepository = mock(ShowRepository.class);
        this.movieRepository = mock(MovieRepository.class);
        this.showService = new ShowService(this.showRepository, this.movieRepository);
    }

    @Test
    void getUpcomingShows_returnsShowsInOrder() {
        // Arrange
        Movie movie = new Movie();
        movie.setMovieId(1L);
        movie.setTitle("Inception");
        movie.setCategory("Sci-Fi");
        movie.setAgeLimit(13);
        movie.setDurationMin(148);

        Theater theater = new Theater();
        theater.setTheaterId(1L);
        theater.setName("Sal 1");

        LocalDateTime now = LocalDateTime.now();

        Show show1 = new Show();
        show1.setShowId(10L);
        show1.setMovie(movie);
        show1.setTheater(theater);
        show1.setStartTime(now.plusHours(1));
        show1.calculateEndTime();

        Show show2 = new Show();
        show2.setShowId(11L);
        show2.setMovie(movie);
        show2.setTheater(theater);
        show2.setStartTime(now.plusHours(2));
        show2.calculateEndTime();

        when(showRepository.findByStartTimeAfterOrderByStartTimeAsc(any()))
                .thenReturn(List.of(show1, show2));

        // Act
        List<UpcomingShowingDTO> result = showService.getUpcomingShows();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("Inception");
        assertThat(result.get(0).theater()).isEqualTo("Sal 1");
        assertThat(result.get(0).showId()).isEqualTo(10L);
        assertThat(result.get(1).showId()).isEqualTo(11L);

        verify(showRepository, times(1)).findByStartTimeAfterOrderByStartTimeAsc(any());
        verifyNoMoreInteractions(showRepository, movieRepository);
    }

    @Test
    void getMovieDetails_returnsMovieDTO() {
        // Arrange
        Movie movie = new Movie();
        movie.setMovieId(1L);
        movie.setTitle("Inception");
        movie.setCategory("Sci-Fi");
        movie.setAgeLimit(13);
        movie.setDurationMin(148);
        movie.setReleaseYear(2010);
        movie.setActors("Leo");
        movie.setDescription("desc");
        movie.setLanguage("English");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Act
        var dto = showService.getMovieDetails(1L);

        // Assert
        assertThat(dto.movieId()).isEqualTo(1L);
        assertThat(dto.title()).isEqualTo("Inception");
        assertThat(dto.ageLimit()).isEqualTo(13);
        assertThat(dto.actors()).isEqualTo("Leo");

        verify(movieRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(movieRepository);
    }
}