package ek.kinoxp.repository;

import ek.kinoxp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByShow_ShowId(Long showId);
}
