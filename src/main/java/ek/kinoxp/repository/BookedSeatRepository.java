package ek.kinoxp.repository;

import ek.kinoxp.model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    List<BookedSeat> findAllByShow_ShowId(Long showId);
    boolean existsByShow_ShowIdAndRowNumberAndSeatNumber(Long showId, int rowNumber, int seatNumber);
}
