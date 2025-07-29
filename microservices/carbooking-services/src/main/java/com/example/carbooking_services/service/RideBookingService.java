package com.example.carbooking_services.service;

import com.example.carbooking_services.model.RideBooking;
import com.example.carbooking_services.repository.RideBookingRepository;
import org.springframework.stereotype.Service;

@Service
public class RideBookingService {

    private final RideBookingRepository rideBookingRepository;

    public RideBookingService(RideBookingRepository rideBookingRepository) {
        this.rideBookingRepository = rideBookingRepository;
    }

    public RideBooking saveBooking(RideBooking booking) {
        return rideBookingRepository.save(booking);
    }
}
