package com.example.carbooking_services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideBookingRequest {
    private String pickup;
    private String destination;
    private String rideType;
    private double fare;
    private double distance;
}
