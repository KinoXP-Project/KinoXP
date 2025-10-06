package ek.kinoxp.Repository;

import ek.kinoxp.Model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Integer>
{
    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
