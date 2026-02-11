package org.example.uberbookingservice.controller;
import org.example.uberbookingservice.dtos.BookingRequestDto;
import org.example.uberbookingservice.dtos.BookingResponseDto;
import org.example.uberbookingservice.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto){
        log.info("Call to CreateBooking");
        return new ResponseEntity<>(bookingService.createBooking(bookingRequestDto),HttpStatus.CREATED);
    }

    @PatchMapping("/update/{bookingId}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable Integer bookingId, @RequestBody BookingRequestDto bookingRequestDto){
        return new ResponseEntity<>(bookingService.updateBooking(bookingId, bookingRequestDto),HttpStatus.OK);
    }

}
