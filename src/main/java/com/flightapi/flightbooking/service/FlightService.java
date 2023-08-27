package com.flightapi.flightbooking.service;

import com.flightapi.flightbooking.model.Airport;
import com.flightapi.flightbooking.model.Flight;
import com.flightapi.flightbooking.repository.AirportRepository;
import com.flightapi.flightbooking.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    public List<Flight> searchFlights(Long departureAirportId, Long arrivalAirportId, LocalDateTime departureDate, LocalDateTime returnDate) {
        Airport departureAirport = airportRepository.findById(departureAirportId).orElse(null);
        Airport arrivalAirport = airportRepository.findById(arrivalAirportId).orElse(null);

        if (departureAirport == null || arrivalAirport == null) {
            throw new RuntimeException("Could not find one or both airports.");
        }

        List<Flight> departFlights = flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateBetween(departureAirport, arrivalAirport, departureDate, departureDate);

        if (returnDate != null) {
            List<Flight> returnFlights = flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateBetween(arrivalAirport, departureAirport, returnDate, returnDate);
            departFlights.addAll(returnFlights);
        }

        return departFlights;
    }
}
