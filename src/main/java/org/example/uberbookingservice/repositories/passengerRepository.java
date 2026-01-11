package org.example.uberbookingservice.repositories;

import com.example.EntityService.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface passengerRepository extends JpaRepository<passenger,Long> {
}
