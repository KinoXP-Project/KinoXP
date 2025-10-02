package ek.kinoxp.repository;

import ek.kinoxp.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByStartAtAfterOrderByStartAtAsc(LocalDateTime now);
    //findByStartAtAfterOrderByStartAtAsc = Find alle shows hvor startAt er efter en bestemt tid, og sorter stigende efter startAt
    //future shows, sorteret stigende
}


