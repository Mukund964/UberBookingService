package org.example.uberbookingservice.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyDriverLocationDto {
    private Double Latitude;
    private Double Longitude;
}
