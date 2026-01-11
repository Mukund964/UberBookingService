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

    private ExactLocation startLocation;

    private ExactLocation endLocation;
}
