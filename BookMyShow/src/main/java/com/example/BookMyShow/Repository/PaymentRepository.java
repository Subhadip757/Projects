package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.Booking;
import com.example.BookMyShow.Model.Movie;
import com.example.BookMyShow.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);


}
