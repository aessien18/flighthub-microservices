package com.flighthub.Flight;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.OffsetDateTime;

@Service
public class FlightService {

    @Value("${aviationstack.api.key}")
    private String apiKey;

    private final AirportService airportService;
    private final WebClient webClient;

    public FlightService(AirportService airportService, WebClient webClient) {
        this.airportService = airportService;
        this.webClient = webClient;
    }

    public Flux<FlightInfo> searchFlights(String number, String airline, String airport, String date) {
        if ((number == null || number.isEmpty()) && (airline == null || airline.isEmpty()) && (airport == null || airport.isEmpty())) {
            return Flux.empty();
        }   return fetchFlightData(number, airline, airport, date)
                .flatMapMany(this::processFlightData);
    }

    public Mono<FlightInfo> getRandomFlight() {
        return fetchFlightData(null, null, null, null)
                .flatMapMany(this::processFlightData)
                .collectList()
                .flatMap(flights -> {
                    if (flights.isEmpty()) {
                        return Mono.empty();
                    }
                    int randomIndex = (int) (Math.random() * flights.size());
                    return Mono.just(flights.get(randomIndex));
                });
    }

    private Mono<JsonNode> fetchFlightData(String number, String airline, String airport, String date) {
        String baseUrl = "http://api.aviationstack.com/v1/flights";
        return webClient.get()
                .uri(baseUrl, uriBuilder -> {
                    uriBuilder.queryParam("access_key", apiKey);
                    if (number != null && !number.isEmpty()) {
                        uriBuilder.queryParam("flight_iata", number);
                    } else if (airline != null && !airline.isEmpty()) {
                        uriBuilder.queryParam("airline_iata", airline);
                    } else if (airport != null && !airport.isEmpty()) {
                        uriBuilder.queryParam("dep_iata", airport);
                    }
                    if (date != null && !date.isEmpty()) {
                        uriBuilder.queryParam("flight_date", date);
                    }
                    if (number == null && airline == null && airport == null && date == null) {
                        uriBuilder.queryParam("limit", 100);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(e -> {
                    throw new FlightServiceException("Error fetching flight data from aviationstack API", e);
                });
    }

    private Flux<FlightInfo> processFlightData(JsonNode root) {
        JsonNode data = root.path("data");
        if (data.isArray()) {
            return Flux.fromIterable(data)
                    .flatMap(this::createFlightInfo);
        }
        return Flux.empty();
    }

    private Mono<FlightInfo> processSingleFlight(JsonNode root) {
        JsonNode data = root.path("data");
        if (data.isArray() && data.size() > 0) {
            return createFlightInfo(data.get(0));
        }
        return Mono.empty();
    }

    private Mono<FlightInfo> createFlightInfo(JsonNode flight) {
        String depIata = flight.path("departure").path("iata").asText();
        String arrIata = flight.path("arrival").path("iata").asText();

        AirportInfo depAirport = airportService.getAirportByIata(depIata);
        AirportInfo arrAirport = airportService.getAirportByIata(arrIata);

        if (depAirport == null) {
            System.out.println("Could not find departure airport for IATA: " + depIata);
            return Mono.empty();
        }
        if (arrAirport == null) {
            System.out.println("Could not find arrival airport for IATA: " + arrIata);
            return Mono.empty();
        }

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

        info.setDepartureLatitude(depAirport.getLatitude());
        info.setDepartureLongitude(depAirport.getLongitude());
        info.setDepartureCity(depAirport.getCity());
        info.setDepartureCountry(depAirport.getCountry());

        info.setArrivalLatitude(arrAirport.getLatitude());
        info.setArrivalLongitude(arrAirport.getLongitude());
        info.setArrivalCity(arrAirport.getCity());
        info.setArrivalCountry(arrAirport.getCountry());

        String scheduled = flight.path("arrival").path("scheduled").asText();
        String actual = flight.path("arrival").path("actual").asText();
        int delayMinutes = calculateDelayMinutes(scheduled, actual);
        info.setDelayMinutes(delayMinutes);

        return Mono.just(info);
    }

    private int calculateDelayMinutes(String scheduled, String actual) {
        if (scheduled == null || scheduled.isEmpty() || actual == null || actual.isEmpty()) {
            return 0;
        }

        try {
            OffsetDateTime scheduledTime = OffsetDateTime.parse(scheduled);
            OffsetDateTime actualTime = OffsetDateTime.parse(actual);

            long diffMinutes = Duration.between(scheduledTime, actualTime).toMinutes();
            return (int) diffMinutes;

        } catch (Exception e) {
            // Log the error appropriately
            return 0;
        }
    }
}
