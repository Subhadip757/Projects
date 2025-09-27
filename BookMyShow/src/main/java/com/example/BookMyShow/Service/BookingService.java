package com.example.BookMyShow.Service;

import com.example.BookMyShow.DTO.*;
import com.example.BookMyShow.Exception.ResourceNotFound;
import com.example.BookMyShow.Exception.SeatUnavailable;
import com.example.BookMyShow.Model.*;
import com.example.BookMyShow.Repository.BookingRepository;
import com.example.BookMyShow.Repository.ShowRepository;
import com.example.BookMyShow.Repository.ShowSeatRepository;
import com.example.BookMyShow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public BookingDTO createBooking(BookingRequestDTO bookingRequest){
        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        Show show = showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(() -> new ResourceNotFound("Show not found"));

        List<ShowSeat> selectedSeats = showSeatRepository.findAllById(bookingRequest.getSeatIds());

        for(ShowSeat seat : selectedSeats){
            if("!AVAILABLE".equals(seat.getStatus())){
                throw new SeatUnavailable("Seat " + seat.getSeat().getSeatNumber() + " is not available");
            }
            seat.setStatus("LOCKED");
        }
        showSeatRepository.saveAll(selectedSeats);

        Double totalAmount = selectedSeats.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentMethod(bookingRequest.getPaymentMethod());
        payment.setStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        //booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setTotalAmount(totalAmount);
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setPayment(payment);

        Booking saveBooking = bookingRepository.save(booking);

        selectedSeats.forEach(seat -> {
            seat.setStatus("BOOKED");
            seat.setBooking(saveBooking);
        });
        showSeatRepository.saveAll(selectedSeats);
        return mapToBookingDto(saveBooking, selectedSeats);
    }

    public BookingDTO getBookingById(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new ResourceNotFound("Booking not found!!"));
        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }

    private BookingDTO getBookingByNumber(String bookingNumber){
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(()->new ResourceNotFound("Booking not found!!"));
        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }

    private List<BookingDTO> getBookingByUser(Long id){
        List<Booking> bookings = bookingRepository.findByUserId(id);

        return bookings.stream().map(booking -> {
            List<ShowSeat> seats = showSeatRepository.findAll()
                    .stream()
                    .filter(seat -> seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                    .collect(Collectors.toList());
            return mapToBookingDto(booking, seats);
        })
        .collect(Collectors.toList());
    }

    @Transactional
    public BookingDTO cancelBooking(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Booking not found"));
        booking.setStatus("CANCELLED");
        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        seats.forEach(seat -> {
            seat.setStatus("AVAILABLE");
            seat.setBooking(null);
        });

        if(booking.getPayment() != null){
            booking.getPayment().setStatus("REFUNDED");
        }

        Booking updateBooking = bookingRepository.save(booking);
        showSeatRepository.saveAll(seats);

        return mapToBookingDto(updateBooking, seats);
    }


    private BookingDTO mapToBookingDto(Booking booking, List<ShowSeat> seats){
        BookingDTO bookingDto = new BookingDTO();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

        // User
        UserDTO userDTO = new UserDTO();
        userDTO.setId(booking.getUser().getId());
        userDTO.setName(booking.getUser().getName());
        userDTO.setEmail(booking.getUser().getEmail());
        userDTO.setPhoneNumber(booking.getUser().getPhoneNumber());
        bookingDto.setUser(userDTO);

        ShowDTO showDTO = new ShowDTO();
        showDTO.setId(booking.getShow().getId());
        showDTO.setStartTime(booking.getShow().getStartTime());
        showDTO.setEndTime(booking.getShow().getEndTime());

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(booking.getShow().getMovie().getId());
        movieDTO.setTitle(booking.getShow().getMovie().getTitle());
        movieDTO.setDescription(booking.getShow().getMovie().getDescription());
        movieDTO.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDTO.setGenre(booking.getShow().getMovie().getGenre());
        movieDTO.setDurationMins(booking.getShow().getMovie().getDurationMins());
        movieDTO.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        movieDTO.setPosterUrl(booking.getShow().getMovie().getPosterUrl());

        showDTO.setMovie(movieDTO);

        ScreenDTO screenDTO = new ScreenDTO();
        screenDTO.setId(booking.getShow().getScreen().getId());
        screenDTO.setName(booking.getShow().getScreen().getName());
        screenDTO.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

        TheatreDTO theatreDTO = new TheatreDTO();
        theatreDTO.setId(booking.getShow().getScreen().getTheatre().getId());
        theatreDTO.setName(booking.getShow().getScreen().getTheatre().getName());
        theatreDTO.setAddress(booking.getShow().getScreen().getTheatre().getAddress());
        theatreDTO.setCity(booking.getShow().getScreen().getTheatre().getCity());
        theatreDTO.setTotalScreens(booking.getShow().getScreen().getTheatre().getTotalScreen());

        screenDTO.setTheatre(theatreDTO);
        showDTO.setScreen(screenDTO);
        bookingDto.setShow(showDTO);


        List<ShowSeatDTO> seatDTOS = seats.stream()
                .map(seat -> {
                    ShowSeatDTO seatDTO = new ShowSeatDTO();
                    seatDTO.setId(seat.getId());
                    seatDTO.setStatus(seat.getStatus());
                    seatDTO.setPrice(seat.getPrice());

                    SeatDTO baseSeatDto = new SeatDTO();
                    baseSeatDto.setId(seat.getSeat().getId());
                    baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                    baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());

                    seatDTO.setSeat(baseSeatDto);
                    return seatDTO;
                })
                .collect(Collectors.toList());

        bookingDto.setSeats(seatDTOS);

        if(booking.getPayment() != null){
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(booking.getPayment().getId());
            paymentDTO.setAmount(booking.getPayment().getAmount());
            paymentDTO.setPaymentMethod(booking.getPayment().getPaymentMethod());
            paymentDTO.setPaymentTime(booking.getPayment().getPaymentTime());
            paymentDTO.setStatus(booking.getPayment().getStatus());
            paymentDTO.setTransactionId(booking.getPayment().getTransactionId());
            bookingDto.setPayment(paymentDTO);
        }
        return bookingDto;
    }

}
