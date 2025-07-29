package com.flighthub.Flight;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

@Service
public class AirlineService {
    public Flux<AirlineInfo> getAllAirlines() {
        try {
            File file = new ClassPathResource("airlines.csv").getFile();
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<AirlineInfo> iterator = mapper.readerFor(AirlineInfo.class).with(schema).readValues(file);
            return Flux.fromIterable(iterator.readAll());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read airlines.csv", e);
        }
    }
}
