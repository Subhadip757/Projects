package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByLanguage(String lang);

    List<Movie> findByGenre(String genre);

    List<Movie> findByTitleContaining(String title);
}
