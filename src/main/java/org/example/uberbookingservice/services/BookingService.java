package org.example.uberbookingservice.services;


import com.example.EntityService.Models.Booking;
import org.example.uberbookingservice.dtos.BookingRequestDto;
import org.example.uberbookingservice.dtos.BookingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);
}
