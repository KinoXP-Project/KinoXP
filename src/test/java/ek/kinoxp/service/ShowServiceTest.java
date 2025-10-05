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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShowServiceTest {
    private ShowRepository showRepository;
    private MovieRepository movieRepository;
    private ShowService showService;

    @BeforeEach //annotated method should be executed before each @Test
    void setUp() {
        this.showRepository = mock(ShowRepository.class);   // Fake repo
        this.movieRepository = mock(MovieRepository.class);  // Fake repo
        this.showService = new ShowService(this.showRepository, this.movieRepository); // Service vi tester
    }

    @Test
    void getUpcomingShowASC() {
        // Arrange
        Movie movie   = new Movie(1L,"Inception","Sci-Fi",13,148,2010,"Leo","desc","English");
        Theater theater = new Theater(1L,10,100,"Sal 1");
        Show s1 = new Show(10L, movie, theater, LocalDateTime.now().plusHours(1));
        Show s2 = new Show(11L, movie, theater, LocalDateTime.now().plusHours(2));

        when(showRepository.findByStartAtAfterOrderByStartAtAsc(any()))
                .thenReturn(List.of(s1, s2));

    // Act
    List<UpcomingShowingDTO> result = showService.getUpcoming();

    // Assert (AssertJ)
    assertThat(result).hasSize(2);
    assertThat(result.get(0).title()).isEqualTo("Inception");
    assertThat(result.get(0).theater()).isEqualTo("Sal 1");
    assertThat(result.get(0).showId()).isEqualTo(10L);
    assertThat(result.get(1).showId()).isEqualTo(11L);

    verify(showRepository, times(1))
            .findByStartAtAfterOrderByStartAtAsc(any());
    verifyNoMoreInteractions(showRepository, movieRepository);
}
    @Test
    void getMovieDetails() {
    }
}