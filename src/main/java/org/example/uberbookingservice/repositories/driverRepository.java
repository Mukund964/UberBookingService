package org.example.uberbookingservice.repositories;

import com.example.EntityService.Models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface driverRepository extends JpaRepository<Driver, Integer> {
}
