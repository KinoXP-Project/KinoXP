package ek.kinoxp.controller;

import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.ShowDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.service.ProgramService;
import ek.kinoxp.service.ShowService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/program")
@CrossOrigin(origins = "http://127.0.0.1:5500") // eller det portnummer vi åbner index.html fra

public class ProgramController
{
    private final ProgramService programService;
    private final ShowService showService;

    public ProgramController(ProgramService programService, ShowService showService){
        this.programService = programService;
        this.showService = showService;
    }


    @GetMapping
    public List<ShowDTO> getProgram(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, // Fortæller Spring, at datoen i URL’en skal tolkes som ISO format (yyyy-MM-dd)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        {
            LocalDate s = (start != null) ? start : LocalDate.now().withDayOfMonth(1);
            LocalDate e = (end != null) ? end : s.withDayOfMonth(s.lengthOfMonth());
            return programService.getProgram(s, e);
        }
    }

    @GetMapping("/upcoming")
    public List<UpcomingShowingDTO> getUpcoming() {
        return showService.getUpcomingShows();
    }

    @GetMapping("/movies/{id}")
    public MovieDetailDTO getMovie(@PathVariable Long id) {
        return showService.getMovieDetails(id);
    }

    //Custom error handler
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException ex) { // Henter message fra Servicelaget og returnerer
        return ex.getMessage();
    }
}
