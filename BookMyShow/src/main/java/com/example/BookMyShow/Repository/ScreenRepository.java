package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.Payment;
import com.example.BookMyShow.Model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {


    List<Screen> findByTheatreId(Long id);

}
