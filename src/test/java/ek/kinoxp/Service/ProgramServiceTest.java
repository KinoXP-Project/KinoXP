package ek.kinoxp.Service;

import ek.kinoxp.Model.Movie;
import ek.kinoxp.Model.Program;
import ek.kinoxp.Model.Show;
import ek.kinoxp.Model.Theater;
import ek.kinoxp.Repository.ShowRepository;
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

        // Brug den ProgramService-konstruktør DU har
        // Hvis din ProgramService kræver flere repos, giv dem her.
        programService = new ProgramService(showRepository);
        // eller fx: new ProgramService(showRepository, movieRepository);
    }

    @Test
    void getProgram_ReturnsShowsWithin3Months() {
        // Arrange - Vi opsætter testdata og “faker” repository-svaret med Mockito.
        // Opret et show indenfor 3 måneder
        Movie movie = new Movie(1, "Inception", "Sci-Fi", 148, 13, 2010, "English");


        //No args eller setters, idet at vi ikke vil have at den skal tage imod parametre
        Theater theater = new Theater(); // no-args + setters
        theater.setName("Theater 1");

        Program program = new Program(); // no-args + setters
        program.setMovie(movie);
        program.setTheater(theater);


        Show show = new Show(); // no-args + setters
        show.setProgram(program); // DTO’en læser via s.getProgram().getMovie()
        show.setStartTime(LocalDateTime.of(2025, 10, 14, 20, 0));
        show.setEndTime  (LocalDateTime.of(2025, 10, 14, 23, 0));

        // Repository skal returnere dette show
        when(showRepository.findByStartTimeBetween(
                any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(show));

        // Act - programService.getProgram(...).
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

