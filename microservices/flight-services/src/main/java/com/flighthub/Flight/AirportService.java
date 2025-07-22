package com.flighthub.Flight;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class AirportService {

    private final Map<String, AirportInfo> airportMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            getClass().getResourceAsStream("/airports.dat")
                    )
            );

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length >= 8) {
                    String name = parts[1].replace("\"", "");
                    String city = parts[2].replace("\"", "");
                    String country = parts[3].replace("\"", "");
                    String iata = parts[4].replace("\"", "");
                    String icao = parts[5].replace("\"", "");
                    double latitude = Double.parseDouble(parts[6]);
                    double longitude = Double.parseDouble(parts[7]);

                    if (iata != null && !iata.isEmpty() && !iata.equals("\\N")) {
                        airportMap.put(iata, new AirportInfo(name, iata, icao, latitude, longitude, city, country));
                    }
                }
            }
            reader.close();
            System.out.println("✅ Loaded " + airportMap.size() + " airports");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AirportInfo getAirportByIata(String iata) {
        return airportMap.get(iata);
    }

    public Map<String, AirportInfo> getAllAirports() {
        return airportMap;
    }
}
