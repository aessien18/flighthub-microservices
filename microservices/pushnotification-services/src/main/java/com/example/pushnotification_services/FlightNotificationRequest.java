package com.example.pushnotification_services;

public class FlightNotificationRequest {
    private String title;
    private String body;
    private String flightNumber;
    private String gate;
    private String status;
    private String time;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
