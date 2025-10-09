package ek.kinoxp.controller;

import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Program;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import ek.kinoxp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingShowInfoIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired TheaterRepository theaterRepo;
    @Autowired MovieRepository movieRepo;
    @Autowired ProgramRepository programRepo;
    @Autowired ShowRepository showRepo;

    Long showId;

    @BeforeEach
    void seed() {
        jdbc.execute("SET REFERENTIAL_INTEGRITY FALSE");
        showRepo.deleteAllInBatch();
        programRepo.deleteAllInBatch();
        movieRepo.deleteAllInBatch();
        theaterRepo.deleteAllInBatch();
        jdbc.execute("SET REFERENTIAL_INTEGRITY TRUE");

        Theater t = new Theater();
        t.setName("Sal 2"); t.setRowCount(7); t.setSeatCount(9);
        t = theaterRepo.save(t);

        Movie m = new Movie();
        m.setTitle("Test Film"); m.setDurationMin(100);
        m = movieRepo.save(m);

        Program p = new Program();
        p.setStartDate(LocalDate.now()); p.setEndDate(LocalDate.now().plusDays(5));
        p = programRepo.save(p);

        Show s = new Show();
        s.setTheater(t); s.setMovie(m); s.setProgram(p);
        s.setStartTime(LocalDateTime.now().plusDays(2));
        s.calculateEndTime();
        s = showRepo.save(s);
        showId = s.getShowId();
    }


    @Test
    void showInfo_returnsRowsAndSeatsPerRow() throws Exception {
        mvc.perform(get("/bookings/show-info")
                        .param("showId", String.valueOf(showId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rowCount").value(7))
                .andExpect(jsonPath("$.seatCount").value(9))
                .andExpect(jsonPath("$.theaterName").value("Sal 2"));
    }
}
