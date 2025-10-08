package ek.kinoxp.service;

import ek.kinoxp.dto.SearchShowingDTO;
import ek.kinoxp.repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ShowService
{
    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository)
    {
        this.showRepository = showRepository;
    }

    public List<SearchShowingDTO> searchShows(
            String title,
            String category,
            LocalDate start,   // dato uden klokkeslæt
            LocalDate end,
            Integer theaterId,
            String theaterName
    ) {
        LocalDateTime startAt = (start != null) ? start.atStartOfDay() : null;
        LocalDateTime endAt   = (end   != null) ? end.atTime(LocalTime.MAX) : null;

        return showRepository.searchShows(title, category, startAt, endAt, theaterId, theaterName);
    }



}
