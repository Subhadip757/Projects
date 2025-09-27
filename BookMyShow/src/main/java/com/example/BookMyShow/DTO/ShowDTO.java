package com.example.BookMyShow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private MovieDTO movie;
    private ScreenDTO screen;
    private List<ShowSeatDTO> availableSeats;

}
