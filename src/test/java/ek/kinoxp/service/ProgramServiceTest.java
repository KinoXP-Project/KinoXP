package ek.kinoxp.service;

import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Program;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import ek.kinoxp.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProgramServiceTest {

    private ShowRepository showRepository;
    private ProgramService programService;

    @BeforeEach
    void setup() {
        // Mock repo
        showRepository = mock(ShowRepository.class);
        programService = new ProgramService(showRepository);
    }

    @Test
    void getProgram_ReturnsShowsWithin3Months() {
        // Arrange - Vi opsætter testdata og “faker” repository-svaret med Mockito.
        // Opret et show indenfor 3 måneder
        Movie movie = new Movie(1L, "Inception", "Sci-fi", 13, 180, 2013, "Leo", "Description", "English");

        Theater theater = new Theater();
        theater.setName("Theater 1");

        Program program = new Program();

        Show show = new Show();
        show.setProgram(program);
        show.setMovie(movie);
        show.setTheater(theater);
        show.setStartTime(LocalDateTime.of(2025, 10, 14, 20, 0));
        show.setEndTime  (LocalDateTime.of(2025, 10, 14, 23, 0));

        // Repository skal returnere dette show
        when(showRepository.findByStartTimeBetween(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(show));

        // Act
        // Kalder metoden der skal testes
        var result = programService.getProgram(
                LocalDate.of(2025, 10, 1),
                LocalDate.of(2025, 10, 31));


        // Assert - tjekker om returnering er korrekt & at det rigtige show kommer med
        // Tjekker at ét show bliver returneret
        assertEquals(1, result.size());

        // Tjekker at titlen er korrekt
        assertEquals("Inception", result.get(0).movieTitle());
    }


    @Test
    void getProgram_SendsCorrectBoundsToRepository() {
        //Arrange – Vi forbereder et gyldigt datointerval (oktober måned)
        LocalDate start = LocalDate.of(2025,10,1);
        LocalDate end   = LocalDate.of(2025,10,31);

        // Vi laver et tomt mock-resultat (vi tester kun grænserne, ikke indholdet)
        when(showRepository.findByStartTimeBetween(any(), any()))
                .thenReturn(List.of());

        // Act – Vi kalder metoden, som skal sende disse datoer videre til repo’et
        programService.getProgram(start, end);

        // Assert – Vi fanger værdierne, der blev sendt til repo’et
        // ArgumentCaptor bruges til at fange de faktiske argumenter, som bliver sendt til en mock-metode.
        // Det gør det muligt at teste, hvilke værdier metoden blev kaldt med (fx start og slut dato).

        var startCap = ArgumentCaptor.forClass(LocalDateTime.class);
        var endCap = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(showRepository).findByStartTimeBetween(startCap.capture(), endCap.capture());

        // Vi tjekker at starttid = 2025-10-01T00:00 og sluttid = 2025-10-31T23:59:59
        assertEquals(start.atStartOfDay(), startCap.getValue());
        assertEquals(end.atTime(LocalTime.MAX), endCap.getValue());
    }

    //Negativ test, for at se om den tager imod shows over 3 måneder
    @Test
    void getProgram_RejectsWhenBeyond3Months() {
        // Arrange – Vi sætter et interval, som ligger 4 måneder ude i fremtiden
        LocalDate start = LocalDate.now().plusMonths(4);
        LocalDate end   = start.plusDays(1);

        // Act & Assert – Vi forventer, at metoden kaster IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> programService.getProgram(start, end));

        // Dette tester User Story-kravet:
        // “Backend skal afvise kald, hvis datoerne ligger mere end 3 måneder frem”
    }
}

