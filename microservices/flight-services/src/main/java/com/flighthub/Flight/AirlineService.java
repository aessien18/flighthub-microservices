package com.flighthub.Flight;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirlineService {

    private final List<AirlineInfo> airlineList = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    getClass().getResourceAsStream("/airlines.csv")
                )
            );

            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header
                    continue;
                }

                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                if (parts.length >= 4) {
                    String name = parts[0].replace("\"", "").trim();
                    String iata = parts[1].replace("\"", "").trim();
                    String icao = parts[2].replace("\"", "").trim();
                    String logo = parts[3].replace("\"", "").trim();

                    airlineList.add(new AirlineInfo(name, iata, icao, logo));
                }
            }
            reader.close();

            System.out.println("Loaded " + airlineList.size() + " airlines!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AirlineInfo> getAllAirlines() {
        return airlineList;
    }
}
