package ek.kinoxp.service;

import ek.kinoxp.dto.ShowDTO;
import ek.kinoxp.model.Show;
import ek.kinoxp.repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ProgramService
{
    private final ShowRepository showRepository;

    public ProgramService(ShowRepository showRepository){
        this.showRepository = showRepository;
    }

    public List<ShowDTO> getProgram(LocalDate start, LocalDate end){
        if (end.isBefore(start)){
            throw new IllegalArgumentException("end must be on/after start");
        }
        LocalDate max = LocalDate.now().plusMonths(3);

        if (start.isAfter(max) || end.isAfter(max)) {
            throw new IllegalArgumentException("Program can only be shown up to 3 months ahead");
        }

        List<Show> shows = showRepository.findByStartTimeBetween(
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        );
        return shows.stream().map(ShowDTO::from).toList(); //genvej til at pege på en metode i stedet for selv at skrive lambda-udtrykket.
    }
}
