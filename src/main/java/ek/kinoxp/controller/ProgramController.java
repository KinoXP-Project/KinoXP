package ek.kinoxp.controller;


import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.service.ShowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin //(@CrossOrigin origins = "${cors.allowed.orgins:http://localhost:5501}")
@RequestMapping("api/program") //
public class ProgramController {

    private final ShowService showService;

    public ProgramController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/upcoming")
    public List<UpcomingShowingDTO> getUpcoming() {
        return showService.getUpcoming();
    }

    @GetMapping("/movies/{id}")
    public MovieDetailDTO getMovie(@PathVariable Long id) {
        return showService.getMovieDetails(id);
    }
}