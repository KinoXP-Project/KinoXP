package ek.kinoxp.Controller;

import ek.kinoxp.DTO.ShowDTO;
import ek.kinoxp.Service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/program")
@CrossOrigin

public class ProgramController
{
    @Autowired
    private ProgramService proService;

    public ProgramController(ProgramService proService){
        this.proService = proService;
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
            return proService.getProgram(s, e);
        }
    }

    //Customer error handler
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException ex) { //henter message fra ServiceLaget og returnere
        return ex.getMessage();
    }
}
