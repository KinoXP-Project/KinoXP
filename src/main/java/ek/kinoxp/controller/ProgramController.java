package ek.kinoxp.controller;

import ek.kinoxp.dto.SearchShowingDTO;
import ek.kinoxp.dto.ShowDTO;
import ek.kinoxp.service.ProgramService;
import ek.kinoxp.service.ShowService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/program")
@CrossOrigin(origins = "http://127.0.0.1:5500") // eller det portnummer du åbner index.html fra

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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, //den fortæller Spring, at datoen i URL’en skal tolkes som ISO format (yyyy-MM-dd)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    )
    {
        {
            LocalDate s = (start != null) ? start : LocalDate.now().withDayOfMonth(1);
            LocalDate e = (end != null) ? end : s.withDayOfMonth(s.lengthOfMonth());
            return programService.getProgram(s, e);
        }
    }

    //Customer error handler
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException ex) { //henter message fra ServiceLaget og returnere
        return ex.getMessage();
    }

    // GET /showings/search?title=...&category=...&start=2025-10-01&end=2025-10-31&theaterId=1&theaterName=sal%201
    @GetMapping("/search")
    //Gør det fleksibelt at kunne søge igennem de forskellige titler med RequestParam(required = false).
    public ResponseEntity<List<SearchShowingDTO>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) Integer theaterId,
            @RequestParam(required = false) String theaterName
    ) {
        List<SearchShowingDTO> results = showService.searchShows(
                title, category, start, end, theaterId, theaterName
        );
        return ResponseEntity.ok(results);
    }
}
