package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByShowId(Long id);
}
