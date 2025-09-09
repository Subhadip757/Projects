package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.Screen;
import com.example.BookMyShow.Model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {


    List<Show> findByMovieId(Long id);
    List<Show> findByScreenId(Long id);

    List<Show> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Show> findByMovie_IdAndScreen_Theatre_City(Long movieId, String city);
}
