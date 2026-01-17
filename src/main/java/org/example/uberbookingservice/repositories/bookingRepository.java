package org.example.uberbookingservice.repositories;

import com.example.EntityService.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface bookingRepository extends JpaRepository<Booking,Integer> {
}
