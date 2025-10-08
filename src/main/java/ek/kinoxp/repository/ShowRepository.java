package ek.kinoxp.repository;

import ek.kinoxp.dto.SearchShowingDTO;
import ek.kinoxp.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long>
{
    //Finder alle shows mellem to tidspunkter
    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Custom søgning med fleksible filtrering
    @Query("""
      SELECT new ek.kinoxp.dto.SearchShowingDTO
            (s.showId, m.title, m.category, t.theaterId, t.name, s.startTime)
        FROM Show s
        JOIN s.movie m
        JOIN s.theater t
        WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
          AND (:category IS NULL OR LOWER(m.category) = LOWER(:category))
          AND (:startAt IS NULL OR s.startTime >= :startAt)
          AND (:endAt IS NULL OR s.startTime <= :endAt)
          AND (:theaterId IS NULL OR t.theaterId = :theaterId)
          AND (:theaterName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :theaterName, '%')))
        ORDER BY s.startTime ASC
      """)

    List<SearchShowingDTO> searchShows(
            @Param("title") String title,
            @Param("category") String category,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("theaterId") Integer theaterId,
            @Param("theaterName") String theaterName
    );
}
