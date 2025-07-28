package com.example.carbooking_services.repository;

import com.example.carbooking_services.model.RideBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideBookingRepository extends JpaRepository<RideBooking, Long> {
}