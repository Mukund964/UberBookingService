package org.example.uberbookingservice.dtos;

import com.example.EntityService.Models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Integer bookingId;
    private String bookingStatus;
    private Optional<Driver> driver;
}
