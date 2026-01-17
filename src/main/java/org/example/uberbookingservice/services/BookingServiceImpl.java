package org.example.uberbookingservice.services;

import com.example.EntityService.Models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.uberbookingservice.apis.LocationServiceApi;
import org.example.uberbookingservice.apis.SocketServiceApi;
import org.example.uberbookingservice.dtos.*;
import org.example.uberbookingservice.repositories.bookingRepository;
import org.example.uberbookingservice.repositories.driverRepository;
import org.example.uberbookingservice.repositories.passengerRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{
    private final bookingRepository bookingRepository;
    private final passengerRepository passengerRepository;
    private final driverRepository driverRepository;

    @PersistenceContext
    private EntityManager entityManager;


    private final LocationServiceApi locationServiceApi;
    private final SocketServiceApi uberSocketApi;

    public BookingServiceImpl(bookingRepository bookingRepo, passengerRepository passengerRepo,LocationServiceApi locationServiceApi,SocketServiceApi socketServiceApi,driverRepository driverRepo){
        this.bookingRepository = bookingRepo;
        this.passengerRepository = passengerRepo;
        this.locationServiceApi = locationServiceApi;
        this.uberSocketApi = socketServiceApi;
        this.driverRepository = driverRepo;
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

    @Override
    public BookingResponseDto updateBooking(Integer bookingId,
                                            BookingRequestDto bookingRequestDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));




        if (bookingRequestDto.getStartLocation() != null) {
            booking.setStartLocation(bookingRequestDto.getStartLocation());
        }

        if (bookingRequestDto.getDriverId() != null) {
            Driver assignedDriver = driverRepository.findById(bookingRequestDto.getDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
            booking.setDriver(assignedDriver);
        }

        if (bookingRequestDto.getBookingStatus() != null) {
            booking.setBookingStatus(
                    BookingStatus.valueOf(bookingRequestDto.getBookingStatus()));
        }
        bookingRepository.save(booking);


        Booking refreshedBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found after update"));

        return BookingResponseDto.builder()
                .bookingId(refreshedBooking.getId())
                .bookingStatus(refreshedBooking.getBookingStatus().name())
                .driver(Optional.ofNullable(refreshedBooking.getDriver()))
                .build();
    }


    public void processNearbyDriversAsync(NearbyDriverLocationDto requestDto, Long passengerId, Integer bookingId){
        Call<driverLocationDto[]> call = locationServiceApi.findNearbyDrivers(requestDto);

        call.enqueue(new Callback<driverLocationDto[]>() {
            @Override
            public void onResponse(@NonNull Call<driverLocationDto[]> call, @NonNull Response<driverLocationDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<driverLocationDto> driverLocation = Arrays.asList(response.body());
                    driverLocation.forEach(driverLocationDto -> {
                        System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() + "long: " + driverLocationDto.getLongitude());
                    });
                }else{
                    System.out.println("Request Failed" + response.message());
                }

                try{
                    raiseRideRequestAsync(RideRequestDto.builder().BookingId(bookingId).PassengerId(passengerId).build());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(@NonNull Call<driverLocationDto[]> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void raiseRideRequestAsync(RideRequestDto rideRequestDto){
        Call<Boolean> call = uberSocketApi.requestNewRide(rideRequestDto);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println(response.isSuccessful());
                System.out.println(response.message());
                if (response.isSuccessful() && response.body() != null) {
                    Boolean result = response.body();
                    System.out.println("Driver response is" + result.toString());

                } else {
                    System.out.println("Request for ride failed" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
