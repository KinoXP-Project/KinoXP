package ek.kinoxp.Service;

import ek.kinoxp.DTO.ShowDTO;
import ek.kinoxp.Model.Show;
import ek.kinoxp.Repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ProgramService
{
    private ShowRepository showRepo;

    public ProgramService(ShowRepository showRepo){
        this.showRepo = showRepo;
    }

    public List<ShowDTO> getProgram(LocalDate start, LocalDate end){
        if (end.isBefore(start)){
            throw new IllegalArgumentException("end must be on/after start");
        }
        LocalDate max = LocalDate.now().plusMonths(3);

        if (start.isAfter(max) || end.isAfter(max)) {
            throw new IllegalArgumentException("Program can only be shown up to 3 months ahead");
        }

        List<Show> shows = showRepo.findByStartTimeBetween(
                start.atStartOfDay(),
                end.atTime(LocalTime.MAX)
        );
        return shows.stream().map(ShowDTO::from).toList(); //genvej til at pege på en metode i stedet for selv at skrive lambda-udtrykket.
    }


}
