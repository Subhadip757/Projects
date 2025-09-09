package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.ShowSeat;
import com.example.BookMyShow.Model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheatreRepository extends JpaRepository<Theatre, Long> {


    List<Theatre> findByShowId(Long movieId);
}
