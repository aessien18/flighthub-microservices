package com.airwise.service;

import com.airwise.model.RideBooking;
import com.airwise.repository.RideBookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideBookingService {

    private final RideBookingRepository repository;

    public RideBookingService(RideBookingRepository repository) {
        this.repository = repository;
    }

    public RideBooking saveBooking(RideBooking booking) {
        return repository.save(booking);
    }

    public List<RideBooking> getAllBookings() {
        return repository.findAll();
    }
}



