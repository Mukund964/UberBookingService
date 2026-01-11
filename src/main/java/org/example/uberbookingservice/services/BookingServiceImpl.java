package org.example.uberbookingservice.services;

import com.example.EntityService.Models.*;
import org.example.uberbookingservice.apis.LocationServiceApi;
import org.example.uberbookingservice.dtos.BookingRequestDto;
import org.example.uberbookingservice.dtos.BookingResponseDto;
import org.example.uberbookingservice.dtos.NearbyDriverLocationDto;
import org.example.uberbookingservice.dtos.driverLocationDto;
import org.example.uberbookingservice.repositories.bookingRepository;
import org.example.uberbookingservice.repositories.passengerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{
    private final bookingRepository bookingRepository;
    private final passengerRepository passengerRepository;


    private final LocationServiceApi locationServiceApi;

    public BookingServiceImpl(bookingRepository bookingRepo, passengerRepository passengerRepo,LocationServiceApi locationServiceApi){
        this.bookingRepository = bookingRepo;
        this.passengerRepository = passengerRepo;
        this.locationServiceApi = locationServiceApi;
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

        processNearbyDriversAsync(request, bookingRequestDto.getPassengerId(),newBooking.getId());

        return BookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();


    }

    public void processNearbyDriversAsync(NearbyDriverLocationDto requestDto, Long passengerId, Integer bookingId){
        Call<driverLocationDto[]> call = locationServiceApi.findNearbyDrivers(requestDto);

        call.enqueue(new Callback<driverLocationDto[]>() {
            @Override
            public void onResponse(Call<driverLocationDto[]> call, Response<driverLocationDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<driverLocationDto> driverLocation = Arrays.asList(response.body());
                    driverLocation.forEach(driverLocationDto -> {
                        System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
                    });
                }else{
                    System.out.println("Request Failed" + response.message());
                }
            }

            @Override
            public void onFailure(Call<driverLocationDto[]> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
