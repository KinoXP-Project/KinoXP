package ek.kinoxp.Service;

import ek.kinoxp.DTO.BookingConfirmationDTO;
import ek.kinoxp.DTO.CreateBookingRequest;
import ek.kinoxp.DTO.SeatDTO;
import ek.kinoxp.DTO.ShowInfoDTO;
import ek.kinoxp.model.BookedSeat;
import ek.kinoxp.model.Booking;
import ek.kinoxp.model.Show;
import ek.kinoxp.repository.BookedSeatRepository;
import ek.kinoxp.repository.BookingRepository;
import ek.kinoxp.repository.ShowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final BookedSeatRepository bookedSeatRepository;
    private final ShowRepository showRepository;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository,
                          BookedSeatRepository bookedSeatRepository,
                          ShowRepository showRepository,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.bookedSeatRepository = bookedSeatRepository;
        this.showRepository = showRepository;
        this.emailService = emailService;
    }

    public ShowInfoDTO getShowInfo(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found: " + showId));

        return new ShowInfoDTO(
                show.getShowId(),
                show.getMovie().getTitle(),
                show.getTheater().getName(),
                show.getTheater().getRowCount(),
                show.getTheater().getSeatCount(),   // fortolket som "seats per row"
                show.getStartTime()
        );
    }

    public List<SeatDTO> getOccupiedSeats(Long showId) {
        return bookedSeatRepository.findAllByShow_ShowId(showId).stream()
                .map(bs -> new SeatDTO(bs.getRowNumber(), bs.getSeatNumber()))
                .toList();
    }

    @Transactional
    public BookingConfirmationDTO createBooking(CreateBookingRequest req) {


        Show show = showRepository.findById(req.getShowId())
                .orElseThrow(() -> new IllegalArgumentException("Show not found: " + req.getShowId()));

        int maxRow = show.getTheater().getRowCount();
        int seatsPerRow = show.getTheater().getSeatCount();

        // Bounds + konflikter
        List<SeatDTO> conflicts = new ArrayList<>();
        for (SeatDTO s : req.getSeats()) {
            if (s.getRow() < 1 || s.getRow() > maxRow || s.getSeat() < 1 || s.getSeat() > seatsPerRow) {
                throw new IllegalArgumentException("Seat out of bounds: row " + s.getRow() + ", seat " + s.getSeat());
            }
            boolean taken = bookedSeatRepository.existsByShow_ShowIdAndRowNumberAndSeatNumber(
                    show.getShowId(), s.getRow(), s.getSeat());
            if (taken) {
                conflicts.add(s);
            }
        }
        if (!conflicts.isEmpty()) {
            throw new SeatAlreadyBookedException(conflicts);
        }


        Booking booking = new Booking();
        booking.setShow(show);
        booking.setCustomerName(req.getCustomerName());
        booking.setCustomerEmail(req.getCustomerEmail());
        booking.setBookingTime(LocalDateTime.now());

        req.getSeats().forEach(s -> {
            BookedSeat bs = new BookedSeat();
            bs.setRowNumber(s.getRow());
            bs.setSeatNumber(s.getSeat());
            booking.addSeat(bs);
        });

        Booking saved = bookingRepository.save(booking);


        try {
            emailService.sendBookingConfirmation(saved);
        } catch (Exception e) {
            log.warn("Email sending failed for booking #{}: {}", saved.getBookingId(), e.getMessage());
        }


        return new BookingConfirmationDTO(
                saved.getBookingId(),
                saved.getBookingTime(),
                show.getShowId(),
                show.getMovie().getTitle(),
                show.getTheater().getName(),
                show.getStartTime(),
                saved.getSeats().stream()
                        .map(bs -> new SeatDTO(bs.getRowNumber(), bs.getSeatNumber()))
                        .toList()
        );
    }


    public static class SeatAlreadyBookedException extends RuntimeException {
        private final List<SeatDTO> conflicts;
        public SeatAlreadyBookedException(List<SeatDTO> conflicts) {
            super("One or more seats are already booked");
            this.conflicts = conflicts;
        }
        public List<SeatDTO> getConflicts() { return conflicts; }
    }
}
