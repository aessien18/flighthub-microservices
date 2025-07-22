package com.flighthub.Flight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    @GetMapping("/airlines")
    public List<AirlineInfo> getAirlines() {
        return airlineService.getAllAirlines();
    }
}
