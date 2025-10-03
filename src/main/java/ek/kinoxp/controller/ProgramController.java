package ek.kinoxp.controller;


import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.service.ShowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/program") //
public class ProgramController {

    private final ShowService showService;

    public ProgramController(ShowService showService) {
        this.showService = showService;
        return "/index"; // viser index-html via. Thymeleaf
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