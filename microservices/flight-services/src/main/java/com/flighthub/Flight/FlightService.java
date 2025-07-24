package com.flighthub.Flight;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    @Value("${aviationstack.api.key}")
    private String apiKey;

    private final AirportService airportService;

    public FlightService(AirportService airportService) {
        this.airportService = airportService;
    }

    public List<FlightInfo> searchFlights(String number, String airline, String airport, String date) {
        String baseUrl = "http://api.aviationstack.com/v1/flights?access_key=" + apiKey;

        if (number != null && !number.isEmpty()) {
            baseUrl += "&flight_iata=" + number;
        } else if (airline != null && !airline.isEmpty()) {
            baseUrl += "&airline_iata=" + airline;
        } else if (airport != null && !airport.isEmpty()) {
            baseUrl += "&dep_iata=" + airport;
        } else {
            return new ArrayList<>();
        }

        if (date != null && !date.isEmpty()) {
            baseUrl += "&flight_date=" + date;
        }

        RestTemplate restTemplate = new RestTemplate();
        String rawJson = restTemplate.getForObject(baseUrl, String.class);

        List<FlightInfo> result = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson);
            JsonNode data = root.path("data");

            if (data.isArray()) {
                for (JsonNode flight : data) {
                    FlightInfo info = new FlightInfo();
                    info.setFlightNumber(flight.path("flight").path("iata").asText());
                    info.setStatus(flight.path("flight_status").asText());
                    info.setDepartureAirport(flight.path("departure").path("airport").asText());
                    info.setDepartureGate(flight.path("departure").path("gate").asText());
                    info.setScheduledDeparture(flight.path("departure").path("scheduled").asText());
                    info.setActualDeparture(flight.path("departure").path("actual").asText());
                    info.setArrivalAirport(flight.path("arrival").path("airport").asText());
                    info.setArrivalGate(flight.path("arrival").path("gate").asText());
                    info.setScheduledArrival(flight.path("arrival").path("scheduled").asText());
                    info.setActualArrival(flight.path("arrival").path("actual").asText());
                    info.setDelay(flight.path("departure").path("delay").asInt());

                    String depIata = flight.path("departure").path("iata").asText();
                    String arrIata = flight.path("arrival").path("iata").asText();

                    AirportInfo depAirport = airportService.getAirportByIata(depIata);
                    AirportInfo arrAirport = airportService.getAirportByIata(arrIata);

                    if (depAirport != null) {
                        info.setDepartureLatitude(depAirport.getLatitude());
                        info.setDepartureLongitude(depAirport.getLongitude());
                        info.setDepartureCity(depAirport.getCity());
                        info.setDepartureCountry(depAirport.getCountry());
                    } else {
                        info.setDepartureLatitude(0);
                        info.setDepartureLongitude(0);
                    }

                    if (arrAirport != null) {
                        info.setArrivalLatitude(arrAirport.getLatitude());
                        info.setArrivalLongitude(arrAirport.getLongitude());
                        info.setArrivalCity(arrAirport.getCity());
                        info.setArrivalCountry(arrAirport.getCountry());
                    } else {
                        info.setArrivalLatitude(0);
                        info.setArrivalLongitude(0);
                    }

                    String scheduled = flight.path("arrival").path("scheduled").asText();
                    String actual = flight.path("arrival").path("actual").asText();
                    int delayMinutes = calculateDelayMinutes(scheduled, actual);
                    info.setDelayMinutes(delayMinutes);

                    result.add(info);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // NEW METHOD FOR RANDOM FLIGHT FEATURE
    public FlightInfo getRandomFlight() {
        // Get a random flight from AviationStack API with limit=1
        String baseUrl = "http://api.aviationstack.com/v1/flights?access_key=" + apiKey + "&limit=1";
        
        RestTemplate restTemplate = new RestTemplate();
        String rawJson = restTemplate.getForObject(baseUrl, String.class);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson);
            JsonNode data = root.path("data");
            
            if (data.isArray() && data.size() > 0) {
                JsonNode flight = data.get(0); // Get the first (and only) flight
                
                FlightInfo info = new FlightInfo();
                
                // Map all the flight data (same logic as searchFlights)
                info.setFlightNumber(flight.path("flight").path("iata").asText());
                info.setStatus(flight.path("flight_status").asText());
                info.setDepartureAirport(flight.path("departure").path("airport").asText());
                info.setDepartureGate(flight.path("departure").path("gate").asText());
                info.setScheduledDeparture(flight.path("departure").path("scheduled").asText());
                info.setActualDeparture(flight.path("departure").path("actual").asText());
                info.setArrivalAirport(flight.path("arrival").path("airport").asText());
                info.setArrivalGate(flight.path("arrival").path("gate").asText());
                info.setScheduledArrival(flight.path("arrival").path("scheduled").asText());
                info.setActualArrival(flight.path("arrival").path("actual").asText());
                info.setDelay(flight.path("departure").path("delay").asInt());

                // Get airport codes for coordinate lookup
                String depIata = flight.path("departure").path("iata").asText();
                String arrIata = flight.path("arrival").path("iata").asText();

                // Get airport information including coordinates
                AirportInfo depAirport = airportService.getAirportByIata(depIata);
                AirportInfo arrAirport = airportService.getAirportByIata(arrIata);

                // Set departure airport data
                if (depAirport != null) {
                    info.setDepartureLatitude(depAirport.getLatitude());
                    info.setDepartureLongitude(depAirport.getLongitude());
                    info.setDepartureCity(depAirport.getCity());
                    info.setDepartureCountry(depAirport.getCountry());
                } else {
                    info.setDepartureLatitude(0);
                    info.setDepartureLongitude(0);
                }

                // Set arrival airport data
                if (arrAirport != null) {
                    info.setArrivalLatitude(arrAirport.getLatitude());
                    info.setArrivalLongitude(arrAirport.getLongitude());
                    info.setArrivalCity(arrAirport.getCity());
                    info.setArrivalCountry(arrAirport.getCountry());
                } else {
                    info.setArrivalLatitude(0);
                    info.setArrivalLongitude(0);
                }

                // Calculate delay minutes
                String scheduled = flight.path("arrival").path("scheduled").asText();
                String actual = flight.path("arrival").path("actual").asText();
                int delayMinutes = calculateDelayMinutes(scheduled, actual);
                info.setDelayMinutes(delayMinutes);

                return info;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null; // Return null if no flight found or error occurred
    }

    private int calculateDelayMinutes(String scheduled, String actual) {
        if (scheduled == null || scheduled.isEmpty() || actual == null || actual.isEmpty()) {
            return 0;
        }

        try {
            java.time.OffsetDateTime scheduledTime = java.time.OffsetDateTime.parse(scheduled);
            java.time.OffsetDateTime actualTime = java.time.OffsetDateTime.parse(actual);

            long diffMinutes = java.time.Duration.between(scheduledTime, actualTime).toMinutes();
            return (int) diffMinutes;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}