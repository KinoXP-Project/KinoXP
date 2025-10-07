package ek.kinoxp.repository;

import ek.kinoxp.model.Show;
import ek.kinoxp.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByStartAtAfterOrderByStartAtAsc(LocalDateTime now);

    boolean existsByTheaterAndStartAtBetween(Theater theater, LocalDateTime from, LocalDateTime to);

    List<Show> findByTheater_TheaterIdAndStartAtBetween(Long theaterId, LocalDateTime from, LocalDateTime to);
}
