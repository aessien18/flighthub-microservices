package com.example.carbooking_services.model;

import jakarta.persistence.*;

@Entity
public class RideBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pickup;
    private String destination;
    private String rideType;
    private double fare;
    private double distance;

    public RideBooking() {}

    public RideBooking(String pickup, String destination, String rideType, double fare, double distance) {
        this.pickup = pickup;
        this.destination = destination;
        this.rideType = rideType;
        this.fare = fare;
        this.distance = distance;
    }

    public Long getId() { return id; }

    public String getPickup() { return pickup; }
    public void setPickup(String pickup) { this.pickup = pickup; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getRideType() { return rideType; }
    public void setRideType(String rideType) { this.rideType = rideType; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
}



