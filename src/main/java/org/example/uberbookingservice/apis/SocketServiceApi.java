package org.example.uberbookingservice.apis;

import org.example.uberbookingservice.dtos.RideRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SocketServiceApi {

    @POST("/api/v1/socket/newride")
    Call<Boolean> requestNewRide(@Body RideRequestDto rideRequestDto);
}
