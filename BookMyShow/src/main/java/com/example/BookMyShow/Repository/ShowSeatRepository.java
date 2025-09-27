package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShow_Id(Long showId);

    List<ShowSeat> findByShow_IdAndStatus(Long showId, String status);
}
