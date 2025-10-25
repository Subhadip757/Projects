package com.example.BookMyShow.Controller;

import com.example.BookMyShow.DTO.MovieDTO;
import com.example.BookMyShow.Service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieDTO> createMovies(@Valid @RequestBody MovieDTO movieDTO){
        return new ResponseEntity<>(movieService.createMovie(movieDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/byLang")
    public ResponseEntity<List<MovieDTO>> getMovieByLanguage(@RequestParam(defaultValue = "English") String language){
        return ResponseEntity.ok(movieService.getMovieByLanguage(language));
    }

    @GetMapping("/byGenre")
    public ResponseEntity<List<MovieDTO>> getMovieByGenre(@RequestParam(defaultValue = "Sci-Fi") String genre){
        return ResponseEntity.ok(movieService.getMovieByGenre(genre));
    }

    @GetMapping("/byTitle")
    public ResponseEntity<List<MovieDTO>> searchMoviesByTitle(@RequestParam(defaultValue = "KGF") String title){
        return ResponseEntity.ok(movieService.searchMovies(title));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @RequestBody MovieDTO movieDTO){
        return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
    }

}
