package org.example.uberbookingservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class driverLocationDto {
    private String driverId;
    private Double Latitude;
    private Double Longitude;
}
