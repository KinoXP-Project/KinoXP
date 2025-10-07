package ek.kinoxp.controller;

import ek.kinoxp.dto.CreateShowingRequestDTO;
import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.ShowingResponseDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${cors.allowed.origins:http://localhost:5500}")
@RequestMapping("/api")
public class ProgramController {

    private final ShowService showService;

    public ProgramController(ShowService showService) {
        this.showService = showService;
    }

    // GET /api/upcoming
    @GetMapping("/upcoming")
    public List<UpcomingShowingDTO> getUpcoming() {
        return showService.getUpcoming();
    }

    // GET /api/movies/{id}
    @GetMapping("/movies/{id}")
    public MovieDetailDTO getMovie(@PathVariable Long id) {
        return showService.getMovieDetails(id);
    }

    // POST /api/showings
    @PostMapping("/showings")
    public ResponseEntity<ShowingResponseDTO> create(@RequestBody CreateShowingRequestDTO request) {
        ShowingResponseDTO created = showService.createShowing(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
