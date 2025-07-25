package com.airwise.controller;

import com.airwise.model.RideBooking;
import com.airwise.service.RideBookingService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideBookingController {

    private final RideBookingService service;

    public RideBookingController(RideBookingService service) {
        this.service = service;
    }

    @PostMapping("/book")
    public ResponseEntity<RideBooking> bookRide(@RequestBody RideBookingRequest request) {
        // Convert request DTO to entity
        RideBooking booking = new RideBooking();
        booking.setPickup(request.getPickup());
        booking.setDestination(request.getDestination());
        booking.setDistance(request.getDistance());
        booking.setFare(request.getFare());
        booking.setRideType(request.getRideType());

        RideBooking saved = service.saveBooking(booking);
        return ResponseEntity.ok(saved); // Return the saved object as JSON
    }

    // ✅ Optional GET endpoint for testing
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllBookings());
    }

    // ✅ Request DTO
    @Data
    static class RideBookingRequest {
        private String pickup;
        private String destination;
        private double distance;
        private double fare;
        private String rideType;
    }
}


