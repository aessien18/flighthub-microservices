package com.example.pushnotification_services;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlightNotificationRequest {

    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Body is required")
    private String body;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Gate is required")
    private String gate;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Time is required")
    private String time;
}
