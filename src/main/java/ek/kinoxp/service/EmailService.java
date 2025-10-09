package ek.kinoxp.Service;

import ek.kinoxp.model.Booking;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    String buildPlainText(Booking booking) {
        String seats = booking.getSeats().stream()

                .map(s -> "Row " + s.getRowNumber() + " Seat " + s.getSeatNumber())
                .collect(Collectors.joining(", "));

        return """
                Booking confirmation #%d
                Movie: %s
                Date & time: %s
                Theater: %s
                Seats (%d): %s
                """.formatted(
                booking.getBookingId(),
                booking.getShow().getMovie().getTitle(),
                booking.getShow().getStartTime(),
                booking.getShow().getTheater().getName(),
                booking.getSeats().size(),
                seats
        );
    }

    public void sendBookingConfirmation(Booking booking) {
        String to = booking.getCustomerEmail();
        if (to == null || to.isBlank()) {
            log.warn("Skipping email: missing customerEmail on booking #{}", booking.getBookingId());
            return;
        }

        String subject = "Your KinoXP booking #" + booking.getBookingId();
        String body = buildPlainText(booking);

        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(msg);
            log.info("Sent booking confirmation to {} for #{}", to, booking.getBookingId());
        } catch (Exception ex) {

            log.warn("Email sending failed for booking #{}: {}", booking.getBookingId(), ex.getMessage());
        }
    }
}
