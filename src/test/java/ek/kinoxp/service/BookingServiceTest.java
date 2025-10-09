package ek.kinoxp.service;

import ek.kinoxp.DTO.BookingConfirmationDTO;
import ek.kinoxp.DTO.CreateBookingRequest;
import ek.kinoxp.DTO.SeatDTO;
import ek.kinoxp.Service.BookingService;
import ek.kinoxp.Service.EmailService;
import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import ek.kinoxp.repository.BookedSeatRepository;
import ek.kinoxp.repository.BookingRepository;
import ek.kinoxp.repository.ShowRepository;
import ek.kinoxp.Service.BookingService.SeatAlreadyBookedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    BookingRepository bookingRepo;
    BookedSeatRepository bookedSeatRepo;
    ShowRepository showRepo;
    EmailService emailService;

    BookingService service;

    Theater theater;
    Movie movie;
    Show show;

    @BeforeEach
    void setup() {
        bookingRepo = mock(BookingRepository.class);
        bookedSeatRepo = mock(BookedSeatRepository.class);
        showRepo = mock(ShowRepository.class);
        emailService = mock(EmailService.class);

        service = new BookingService(bookingRepo, bookedSeatRepo, showRepo, emailService);

        theater = new Theater();
        theater.setName("Sal 1");
        theater.setRowCount(20);
        theater.setSeatCount(12);

        movie = new Movie();
        movie.setTitle("Test Film");
        movie.setDurationMin(120);

        show = new Show();
        show.setShowId(1L);
        show.setTheater(theater);
        show.setMovie(movie);
        show.setStartTime(LocalDateTime.now().plusDays(1));
        show.calculateEndTime();

        when(showRepo.findById(1L)).thenReturn(Optional.of(show));
    }

    @Test
    void createBooking_returnsConfirmation_whenValidRequest() {
        when(bookedSeatRepo.existsByShow_ShowIdAndRowNumberAndSeatNumber(1L, 1, 2)).thenReturn(false);

        when(bookingRepo.save(any())).thenAnswer(inv -> {
            var b = (ek.kinoxp.model.Booking) inv.getArgument(0);
            b.setBookingId(42L);
            b.setBookingTime(LocalDateTime.now());
            return b;
        });

        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Alice");
        req.setCustomerEmail("alice@example.com");
        req.setShowId(1L);
        req.setSeats(List.of(new SeatDTO(1, 2)));

        BookingConfirmationDTO dto = service.createBooking(req);

        assertNotNull(dto);
        assertEquals(42L, dto.bookingId());
        assertEquals(1L, dto.showId());
        assertEquals("Test Film", dto.movieTitle());
        assertEquals("Sal 1", dto.theaterName());
        assertEquals(1, dto.seats().size());
        assertEquals(1, dto.seats().get(0).getRow());
        assertEquals(2, dto.seats().get(0).getSeat());

        verify(emailService, times(1)).sendBookingConfirmation(any());
        verify(bookingRepo, times(1)).save(any());
    }

    @Test
    void createBooking_throwsConflict_whenSeatAlreadyBooked() {
        when(bookedSeatRepo.existsByShow_ShowIdAndRowNumberAndSeatNumber(1L, 5, 7)).thenReturn(true);

        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Bob");
        req.setCustomerEmail("bob@example.com");
        req.setShowId(1L);
        req.setSeats(List.of(new SeatDTO(5, 7)));

        assertThrows(SeatAlreadyBookedException.class, () -> service.createBooking(req));
        verify(emailService, never()).sendBookingConfirmation(any());
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void createBooking_throwsBadRequest_whenSeatOutOfBounds() {
        when(bookedSeatRepo.existsByShow_ShowIdAndRowNumberAndSeatNumber(anyLong(), anyInt(), anyInt()))
                .thenReturn(false);

        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Dora");
        req.setCustomerEmail("dora@example.com");
        req.setShowId(1L);
        req.setSeats(List.of(new SeatDTO(99, 1)));

        assertThrows(IllegalArgumentException.class, () -> service.createBooking(req));
        verify(emailService, never()).sendBookingConfirmation(any());
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void createBooking_emailServiceThrows_doesNotFailBooking() {
        // ingen konflikt
        when(bookedSeatRepo.existsByShow_ShowIdAndRowNumberAndSeatNumber(1L, 2, 3)).thenReturn(false);

        // save sætter id + time
        when(bookingRepo.save(any())).thenAnswer(inv -> {
            var b = (ek.kinoxp.model.Booking) inv.getArgument(0);
            b.setBookingId(77L);
            b.setBookingTime(LocalDateTime.now());
            return b;
        });

        doThrow(new RuntimeException("SMTP down"))
                .when(emailService).sendBookingConfirmation(ArgumentMatchers.any());

        CreateBookingRequest req = new CreateBookingRequest();
        req.setCustomerName("Ida");
        req.setCustomerEmail("ida@example.com");
        req.setShowId(1L);
        req.setSeats(List.of(new SeatDTO(2, 3)));

        BookingConfirmationDTO dto = service.createBooking(req);

        assertNotNull(dto);
        assertEquals(77L, dto.bookingId());
        verify(emailService, times(1)).sendBookingConfirmation(any());
        verify(bookingRepo, times(1)).save(any());
    }
}
