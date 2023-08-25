package com.flightapi.flightbooking.controller;

import com.flightapi.flightbooking.model.Airport;
import com.flightapi.flightbooking.service.AirportService;
import com.flightapi.flightbooking.model.Flight;
import com.flightapi.flightbooking.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirportService airportService;

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.saveFlight(flight);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        if (!flightService.getFlightById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        flight.setId(id);
        return ResponseEntity.ok(flightService.saveFlight(flight));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        if (!flightService.getFlightById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<Flight> searchFlights(@RequestParam Long departureAirportId,
                                      @RequestParam Long arrivalAirportId,
                                      @RequestParam LocalDateTime departureDate,
                                      @RequestParam(required = false) LocalDateTime returnDate) {

        Airport departureAirport = airportService.getAirportById(departureAirportId).orElse(null);
        Airport arrivalAirport = airportService.getAirportById(arrivalAirportId).orElse(null);

        if (departureAirport == null || arrivalAirport == null) {
            // In a real application, we should return a more meaningful error response here.
            throw new IllegalArgumentException("Invalid airport IDs");
        }

        return flightService.searchFlights(departureAirport, arrivalAirport, departureDate, returnDate);
    }

}
