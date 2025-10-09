package ek.kinoxp.Service;

import ek.kinoxp.model.BookedSeat;
import ek.kinoxp.model.Booking;
import ek.kinoxp.model.Movie;
import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailServiceTest {

    @Test
    void buildPlainText_containsAllRequiredFields() {
        Theater t = new Theater();
        t.setName("Sal 1");
        t.setRowCount(5);
        t.setSeatCount(8);

        Movie m = new Movie();
        m.setTitle("Interstellar");

        Show show = new Show();
        show.setTheater(t);
        show.setMovie(m);
        show.setStartTime(LocalDateTime.of(2025, 10, 12, 19, 0));

        BookedSeat s1 = new BookedSeat(); s1.setRowNumber(3); s1.setSeatNumber(4);
        BookedSeat s2 = new BookedSeat(); s2.setRowNumber(3); s2.setSeatNumber(5);

        Booking b = new Booking();
        b.setBookingId(123L);
        b.setShow(show);
        b.setCustomerName("Eva");
        b.setCustomerEmail("eva@example.com");
        b.addSeat(s1);
        b.addSeat(s2);

        EmailService es = new EmailService(Mockito.mock(JavaMailSender.class));

        String txt = es.buildPlainText(b);

        assertThat(txt).contains("Booking confirmation #123");
        assertThat(txt).contains("Movie: Interstellar");
        assertThat(txt).contains("Date & time: 2025-10-12T19:00");
        assertThat(txt).contains("Theater: Sal 1");
        assertThat(txt).contains("Seats (2):");
        assertThat(txt).contains("Row 3 Seat 4");
        assertThat(txt).contains("Row 3 Seat 5");
    }
}
