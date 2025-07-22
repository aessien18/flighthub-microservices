package com.flighthub.Flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/Hello")
    public String Sayhello() {
        return "Hello World from Flighthub";
    }

    @GetMapping("/flight")
    public ResponseEntity<?> getFlight(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) String airport,
            @RequestParam(required = false) String date
    ) {
        List<FlightInfo> flights = flightService.searchFlights(number, airline, airport,date);

        if (flights.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiError("No flights found for your search."));
        }

        return ResponseEntity.ok(flights);
    }
}
