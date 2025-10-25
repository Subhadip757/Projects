package com.example.BookMyShow.Controller;

import com.example.BookMyShow.DTO.BookingDTO;
import com.example.BookMyShow.DTO.BookingRequestDTO;
import com.example.BookMyShow.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequest){
        return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/{number}")
    public ResponseEntity<BookingDTO> getBookingByNumber(@PathVariable String number){
        return ResponseEntity.ok(bookingService.getBookingByNumber(number));
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookingByUser(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingByUser(id));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

}
