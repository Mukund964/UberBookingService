package org.example.uberbookingservice.services;

import com.example.EntityService.Models.*;
import org.example.uberbookingservice.dtos.BookingRequestDto;
import org.example.uberbookingservice.dtos.BookingResponseDto;
import org.example.uberbookingservice.dtos.NearbyDriverLocationDto;
import org.example.uberbookingservice.dtos.driverLocationDto;
import org.example.uberbookingservice.repositories.bookingRepository;
import org.example.uberbookingservice.repositories.passengerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{
    private final bookingRepository bookingRepository;
    private final passengerRepository passengerRepository;
    private final RestTemplate restTemplate;

    public BookingServiceImpl(bookingRepository bookingRepo, passengerRepository passengerRepo){
        this.bookingRepository = bookingRepo;
        this.passengerRepository = passengerRepo;
        this.restTemplate = new RestTemplate();
    }
    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        Optional<passenger> passengerFound = passengerRepository.findById(bookingRequestDto.getPassengerId());
        Booking booking = Booking.builder()
                .BookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .passenger(passengerFound.get())
                .startLocation(bookingRequestDto.getStartLocation())
                .endLocation(bookingRequestDto.getEndLocation())
                .build();

       Booking newBooking = bookingRepository.save(booking);


        //start checking nearby drivers from the location (booking has passenger start location)

        NearbyDriverLocationDto request = NearbyDriverLocationDto.builder()
                .Latitude(bookingRequestDto.getStartLocation().getLatitude())
                .Longitude(bookingRequestDto.getStartLocation().getLongitude())
                .build();

        String LOCATION_SERVICE = "http://localhost:3335";
        ResponseEntity<driverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE + "/api/v1/location/nearby/drivers", request, driverLocationDto[].class);

        if(result.getStatusCode().is2xxSuccessful() && result.getBody() != null){
            List<driverLocationDto> driverLocation = Arrays.asList(result.getBody());
            driverLocation.forEach(driverLocationDto -> {
                System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
            });
        }

        return BookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();




    }
}
