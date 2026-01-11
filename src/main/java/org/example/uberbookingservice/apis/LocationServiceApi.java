package org.example.uberbookingservice.apis;

import org.example.uberbookingservice.dtos.NearbyDriverLocationDto;
import org.example.uberbookingservice.dtos.driverLocationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import retrofit2.Call;
import retrofit2.http.POST;

import java.util.List;

public interface LocationServiceApi {

    @POST("/nearby/drivers")
    public Call<ResponseEntity<List<driverLocationDto>>> findNearbyDrivers(@RequestBody NearbyDriverLocationDto nearbyDriverLocationDto);
}
