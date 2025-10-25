package com.example.BookMyShow.Controller;

import com.example.BookMyShow.DTO.ShowDTO;
import com.example.BookMyShow.Service.ShowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    public ResponseEntity<ShowDTO> createShow(@Valid @RequestBody ShowDTO showDTO){
        return new ResponseEntity<>(showService.createShow(showDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDTO> getShowById(@PathVariable Long id){
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShowDTO>> getAllShows(){
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<List<ShowDTO>> getShowByMovieId(@PathVariable Long movieId){
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    @GetMapping("/{movieId}/{city}")
    public ResponseEntity<List<ShowDTO>> getShowByCity(@PathVariable Long movieId, @PathVariable String city){
        return ResponseEntity.ok(showService.getShowsByCity(movieId, city));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ShowDTO>> getShowsByDateRange(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(showService.getShowsByDateRange(startDate, endDate));
    }


}
