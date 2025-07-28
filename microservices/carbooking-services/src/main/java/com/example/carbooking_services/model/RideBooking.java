package com.example.carbooking_services.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String pickup;

    @NotBlank
    private String destination;

    private double distance;
    private double fare;

    @NotBlank
    private String rideType;

    private LocalDateTime time = LocalDateTime.now();
}
