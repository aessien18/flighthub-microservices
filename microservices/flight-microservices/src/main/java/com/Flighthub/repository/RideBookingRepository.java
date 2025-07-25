package com.airwise.repository;

import com.airwise.model.RideBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideBookingRepository extends JpaRepository<RideBooking, Long> {
}

