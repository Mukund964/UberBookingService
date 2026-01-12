package org.example.uberbookingservice.dtos;

import com.example.EntityService.Models.ExactLocation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDto {
    private Long PassengerId;
    private Integer BookingId;
//    private ExactLocation StartLocation;
//    private ExactLocation EndLocation;
    private List<Long> DriverId;
}
