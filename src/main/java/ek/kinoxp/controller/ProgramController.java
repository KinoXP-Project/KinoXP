package ek.kinoxp.controller;

import ek.kinoxp.dto.CreateShowingRequestDTO;
import ek.kinoxp.dto.MovieDetailDTO;
import ek.kinoxp.dto.ShowingResponseDTO;
import ek.kinoxp.dto.UpcomingShowingDTO;
import ek.kinoxp.service.ShowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5500","http://127.0.0.1:5500"})
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
//    @GetMapping("/showings/{id}")
//    public ShowingResponseDTO getShowing(@PathVariable Long id) {
//        var s = showRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Show not found: " + id));
//        return new ShowingResponseDTO(
//                s.getShowId(),
////                s.getMovie().getMovieId(),
////                s.getMovie().getTitle(),
////                s.getTheater().getTheaterId(),
////                s.getTheater().getName(),
////                s.getStartAt()
////        );
//    }

    // POST /api/showings
    @PostMapping("/showings")
    public ResponseEntity<ShowingResponseDTO> create(@RequestBody CreateShowingRequestDTO request) {
        ShowingResponseDTO created = showService.createShowing(request);
        // Location: /api/showings/{id}
        URI location = URI.create(String.format("/api/showings/%d", created.showId()));
        return ResponseEntity.created(location).body(created);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}