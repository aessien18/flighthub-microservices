package com.example.carbooking_services.service;

import com.example.carbooking_services.model.RideBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideBookingRepository extends JpaRepository<RideBooking, Long> {
}