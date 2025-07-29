package com.example.carbooking_services.controller;

import com.example.carbooking_services.model.RideBooking;
import com.example.carbooking_services.repository.RideBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "*") // Allows React Native to connect
public class RideBookingController {

    private static final Logger logger = LoggerFactory.getLogger(RideBookingController.class);

    @Autowired
    private RideBookingRepository rideBookingRepository;

    @PostMapping("/book")
    public ResponseEntity<?> bookRide(@RequestBody RideBooking ride) {
        try {
            logger.info("📦 Booking ride from '{}' to '{}', type: {}, fare: {}, distance: {}km",
                    ride.getPickup(), ride.getDestination(), ride.getRideType(), ride.getFare(), ride.getDistance());

            RideBooking savedRide = rideBookingRepository.save(ride);

            logger.info("✅ Ride booked successfully: {}", savedRide.getId());
            return ResponseEntity.ok(savedRide);
        } catch (Exception e) {
            logger.error("❌ Failed to book ride", e);
            return ResponseEntity.status(500).body("Failed to book ride: " + e.getMessage());
        }
    }
}


