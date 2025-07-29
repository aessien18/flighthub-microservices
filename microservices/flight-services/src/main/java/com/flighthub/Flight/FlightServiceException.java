package com.flighthub.Flight;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FlightServiceException extends RuntimeException {
    public FlightServiceException(String message) {
        super(message);
    }

    public FlightServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
