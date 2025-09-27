package com.example.BookMyShow.Service;

import com.example.BookMyShow.DTO.*;
import com.example.BookMyShow.Exception.ResourceNotFound;
import com.example.BookMyShow.Model.Movie;
import com.example.BookMyShow.Model.Screen;
import com.example.BookMyShow.Model.Show;
import com.example.BookMyShow.Model.ShowSeat;
import com.example.BookMyShow.Repository.MovieRepository;
import com.example.BookMyShow.Repository.ScreenRepository;
import com.example.BookMyShow.Repository.ShowRepository;
import com.example.BookMyShow.Repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    public ShowDTO createShow(ShowDTO showDTO) {
        Show show = new Show();
        Movie movie = movieRepository.findById(showDTO.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFound("Movie not found"));

        Screen screen = screenRepository.findById(showDTO.getScreen().getId())
                .orElseThrow(() -> new ResourceNotFound("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDTO.getStartTime());
        show.setEndTime(showDTO.getEndTime());

        Show savedShow = showRepository.save(show);

        List<ShowSeat> availableSeats = showSeatRepository
                .findByShow_IdAndStatus(savedShow.getId(), "AVAILABLE");

        return mapToDto(savedShow, availableSeats);
    }

    public ShowDTO getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Show not found with id: " + id));

        List<ShowSeat> availableSeats = showSeatRepository
                .findByShow_IdAndStatus(show.getId(), "AVAILABLE");
        return mapToDto(show, availableSeats);
    }

    public List<ShowDTO> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShow_IdAndStatus(show.getId(),
                            "AVAILABLE");
                    return mapToDto(show, availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDTO> getShowsByMovie(Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShow_IdAndStatus(show.getId(),
                            "AVAILABLE");
                    return mapToDto(show, availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDTO> getShowsByCity(Long movieId, String city) {
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theatre_City(movieId, city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShow_IdAndStatus(show.getId(),
                            "AVAILABLE");
                    return mapToDto(show, availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDTO> getShowsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Show> shows = showRepository.findByStartTimeBetween(startDate, endDate);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findByShow_IdAndStatus(show.getId(),
                            "AVAILABLE");
                    return mapToDto(show, availableSeats);
                })
                .collect(Collectors.toList());
    }

    private ShowDTO mapToDto(Show show, List<ShowSeat> availableSeats) {
        ShowDTO showDTO = new ShowDTO();

        showDTO.setId(show.getId());
        showDTO.setStartTime(show.getStartTime());
        showDTO.setEndTime(show.getEndTime());
        showDTO.setMovie(new MovieDTO(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurationMins(),
                show.getMovie().getPosterUrl()));

        TheatreDTO theatreDTO = new TheatreDTO(
                show.getScreen().getTheatre().getId(),
                show.getScreen().getTheatre().getName(),
                show.getScreen().getTheatre().getAddress(),
                show.getScreen().getTheatre().getCity(),
                show.getScreen().getTheatre().getTotalScreen());

        showDTO.setScreen(new ScreenDTO(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                theatreDTO));

        List<ShowSeatDTO> seatDtos = availableSeats.stream().map(seat -> {
            ShowSeatDTO seatDTO = new ShowSeatDTO();
            seatDTO.setId(seat.getId());
            seatDTO.setStatus(seat.getStatus());
            seatDTO.setPrice(seat.getPrice());

            SeatDTO baseSeatDTO = new SeatDTO();
            baseSeatDTO.setId(seat.getSeat().getId());
            baseSeatDTO.setSeatNumber(seat.getSeat().getSeatNumber());
            baseSeatDTO.setSeatType(seat.getSeat().getSeatType());
            baseSeatDTO.setBasePrice(seat.getSeat().getBasePrice());

            seatDTO.setSeat(baseSeatDTO);
            return seatDTO;
        })
                .collect(Collectors.toList());

        showDTO.setAvailableSeats(seatDtos);
        return showDTO;
    }
}
