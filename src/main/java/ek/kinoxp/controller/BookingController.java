package ek.kinoxp.controller;

import ek.kinoxp.DTO.BookingConfirmationDTO;
import ek.kinoxp.DTO.CreateBookingRequest;
import ek.kinoxp.DTO.SeatDTO;
import ek.kinoxp.DTO.ShowInfoDTO;
import ek.kinoxp.Service.BookingService;
import ek.kinoxp.Service.BookingService.SeatAlreadyBookedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/show-info")
    public ResponseEntity<ShowInfoDTO> showInfo(@RequestParam Long showId) {
        return ResponseEntity.ok(bookingService.getShowInfo(showId));
    }

    @GetMapping("/occupied")
    public ResponseEntity<List<SeatDTO>> occupied(@RequestParam Long showId) {
        return ResponseEntity.ok(bookingService.getOccupiedSeats(showId));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateBookingRequest req) {
        try {
            BookingConfirmationDTO dto = bookingService.createBooking(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (SeatAlreadyBookedException e) {
            // 409 ved dobbelt-bookede sæder
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Seat(s) already booked", e.getConflicts()));
        } catch (IllegalArgumentException e) {
            // 400 ved domæne-validering (fx out-of-bounds)
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage(), null));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("errors", ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "msg", fe.getDefaultMessage()))
                .toList());
        return body;
    }

    public record ErrorResponse(String message, List<SeatDTO> conflicts) {}
}
