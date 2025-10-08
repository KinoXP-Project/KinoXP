package ek.kinoxp.repository;

import ek.kinoxp.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long>
{
    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Show> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime startTime);
}
