package org.example.uberbookingservice.controller;

import com.example.EntityService.Models.Booking;
import org.example.uberbookingservice.dtos.BookingRequestDto;
import org.example.uberbookingservice.dtos.BookingResponseDto;
import org.example.uberbookingservice.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto){
        System.out.println(bookingRequestDto.getPassengerId());
        return new ResponseEntity<>(bookingService.createBooking(bookingRequestDto),HttpStatus.CREATED);
    }

}
