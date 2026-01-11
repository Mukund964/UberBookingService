package org.example.uberbookingservice.apis;

import org.example.uberbookingservice.dtos.NearbyDriverLocationDto;
import org.example.uberbookingservice.dtos.driverLocationDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface LocationServiceApi {

    @POST("api/v1/location/nearby/drivers")
    Call<driverLocationDto[]> findNearbyDrivers(@Body NearbyDriverLocationDto nearbyDriverLocationDto);
}
