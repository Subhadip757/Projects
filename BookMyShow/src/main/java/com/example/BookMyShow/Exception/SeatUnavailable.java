package com.example.BookMyShow.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeatUnavailable extends RuntimeException {
    public SeatUnavailable(String message){
        super(message);
    }
}
