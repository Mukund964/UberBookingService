package org.example.uberbookingservice.dtos;


import com.example.EntityService.Models.ExactLocation;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {

    private Long passengerId;
    private Integer driverId;
    private String BookingStatus;

    private ExactLocation startLocation;

    private ExactLocation endLocation;
}
