package com.example.BookMyShow.Controller;

import com.example.BookMyShow.DTO.TheatreDTO;
import com.example.BookMyShow.Service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatre")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @PostMapping
    public ResponseEntity<TheatreDTO> createTheatre(@RequestBody TheatreDTO theatreDTO){
        return new ResponseEntity<>(theatreService.createTheatre(theatreDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TheatreDTO>> getAllTheatres(@PathVariable Long id){
        return ResponseEntity.ok(theatreService.getAllTheatres(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreDTO> getTheatreById(@PathVariable Long id){
        return ResponseEntity.ok(theatreService.getTheatreById(id));
    }

    @GetMapping("/{city}")
    public ResponseEntity<List<TheatreDTO>> getTheatresByCity(@PathVariable String city){
        return ResponseEntity.ok(theatreService.getTheatreByCity(city));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheatreDTO> updateTheater(@PathVariable Long id, @RequestBody TheatreDTO theatreDTO){
        return ResponseEntity.ok(theatreService.updateTheatre(id, theatreDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteTheater(@PathVariable Long id){
        theatreService.deleteTheatre(id);
    }



}
