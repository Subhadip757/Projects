package com.example.BookMyShow.Service;

import com.example.BookMyShow.DTO.MovieDTO;
import com.example.BookMyShow.Exception.ResourceNotFound;
import com.example.BookMyShow.Model.Movie;
import com.example.BookMyShow.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = maptoEntity(movieDTO);
        Movie saveMovie = movieRepository.save(movie);
        return mapToDto(movie);
    }

    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Movie not found " + id));
        return mapToDto(movie);
    }

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getMovieByLanguage(String language) {
        List<Movie> movies = movieRepository.findByLanguage(language);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getMovieByGenre(String genre) {
        List<Movie> movies = movieRepository.findByGenre(genre);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDTO> searchMovies(String title) {
        List<Movie> movies = movieRepository.findByTitleContaining(title);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Movie not found " + id));
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setGenre(movieDTO.getGenre());
        movie.setDurationMins(movieDTO.getDurationMins());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setPosterUrl(movieDTO.getPosterUrl());

        Movie updatedMovie = movieRepository.save(movie);
        return mapToDto(updatedMovie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Movie not found " + id));
        movieRepository.delete(movie);
        // return mapToDto(movie);
    }

    private MovieDTO mapToDto(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setLanguage(movie.getLanguage());
        movieDTO.setGenre(movie.getGenre());
        movieDTO.setDurationMins(movie.getDurationMins());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setPosterUrl(movie.getPosterUrl());
        return movieDTO;
    }

    public Movie maptoEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setGenre(movieDTO.getGenre());
        movie.setDurationMins(movieDTO.getDurationMins());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setPosterUrl(movieDTO.getPosterUrl());
        return movie;
    }
}
