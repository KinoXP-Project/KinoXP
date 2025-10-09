package ek.kinoxp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ek.kinoxp.DTO.CreateBookingRequest;
import ek.kinoxp.DTO.SeatDTO;
import ek.kinoxp.Service.EmailService;

import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Program;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import ek.kinoxp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Autowired JdbcTemplate jdbc;

    @Autowired TheaterRepository theaterRepo;
    @Autowired MovieRepository movieRepo;
    @Autowired ProgramRepository programRepo;
    @Autowired ShowRepository showRepo;
    @Autowired BookingRepository bookingRepo;
    @Autowired BookedSeatRepository bookedSeatRepo;

    @MockBean EmailService emailService;

    Long showId;

    @BeforeEach
    void initData() {
        // Robust FK-cleanup
        jdbc.execute("SET REFERENTIAL_INTEGRITY FALSE");
        bookedSeatRepo.deleteAllInBatch();
        bookingRepo.deleteAllInBatch();
        showRepo.deleteAllInBatch();
        programRepo.deleteAllInBatch();
        movieRepo.deleteAllInBatch();
        theaterRepo.deleteAllInBatch();
        jdbc.execute("SET REFERENTIAL_INTEGRITY TRUE");

        // Seed minimal data
        Theater t = new Theater();
        t.setName("Sal 1");
        t.setRowCount(20);
        t.setSeatCount(12);
        t = theaterRepo.save(t);

        Movie m = new Movie();
        m.setTitle("Integration Test Film");
        m.setDurationMin(100);
        m = movieRepo.save(m);

        Program p = new Program();
        p.setStartDate(LocalDate.now());
        p.setEndDate(LocalDate.now().plusDays(30));
        p = programRepo.save(p);

        Show s = new Show();
        s.setProgram(p);
        s.setMovie(m);
        s.setTheater(t);
        s.setStartTime(LocalDateTime.now().plusDays(1));
        s.calculateEndTime();
        s = showRepo.save(s);

        showId = s.getShowId();


        reset(emailService);
    }

    @Test
    void postBookings_createsAndReturns201() throws Exception {
        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Eva");
        req.setCustomerEmail("eva@example.com");
        req.setShowId(showId);
        req.setSeats(List.of(new SeatDTO(1, 1), new SeatDTO(1, 2)));

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingId").exists())
                .andExpect(jsonPath("$.showId").value(showId))
                .andExpect(jsonPath("$.seats.length()").value(2));
    }

    @Test
    void postBookings_sameSeatTwice_returns409Conflict() throws Exception {
        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Finn");
        req.setCustomerEmail("finn@example.com");
        req.setShowId(showId);
        req.setSeats(List.of(new SeatDTO(5, 5)));

        // første booking går igennem
        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // samme sæde igen -> 409 CONFLICT
        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postBookings_invalidEmail_returns400BadRequest() throws Exception {
        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Gitte");
        req.setCustomerEmail("not-an-email"); // ugyldig email
        req.setShowId(showId);
        req.setSeats(List.of(new SeatDTO(2, 3)));

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postBookings_missingEmail_returns400BadRequest() throws Exception {
        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Hans");
        req.setCustomerEmail(null);
        req.setShowId(showId);
        req.setSeats(List.of(new SeatDTO(3, 4)));

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postBookings_emailServiceThrows_still201() throws Exception {
        doThrow(new RuntimeException("SMTP down"))
                .when(emailService).sendBookingConfirmation(ArgumentMatchers.any());

        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Ida");
        req.setCustomerEmail("ida@example.com");
        req.setShowId(showId);
        req.setSeats(List.of(new SeatDTO(1, 1)));

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}
