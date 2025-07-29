package com.flighthub.Flight;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    private final FlightService flightService;

    public HelloController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/Hello")
    public String Sayhello() {
        return "Hello World from Flighthub";
    }

    @GetMapping("/flight")
    public Flux<FlightInfo> getFlight(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) String airport,
            @RequestParam(required = false) String date
    ) {
        return flightService.searchFlights(number, airline, airport, date);
    }

    // NEW ENDPOINT FOR RANDOM FLIGHT FEATURE
    @GetMapping("/flight/random")
    public Mono<FlightInfo> getRandomFlight() {
        return flightService.getRandomFlight();
    }
}
