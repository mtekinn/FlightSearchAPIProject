package com.flightapi.flightbooking.service;

import com.flightapi.flightbooking.model.Airport;
import com.flightapi.flightbooking.model.Flight;
import com.flightapi.flightbooking.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

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

    public List<Flight> searchFlights(Airport departureAirport, Airport arrivalAirport,
                                      LocalDateTime departureDate, LocalDateTime returnDate) {
        if (returnDate == null) {
            return flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateAndReturnDateIsNull(
                    departureAirport, arrivalAirport, departureDate);
        } else {
            return flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateBetween(
                    departureAirport, arrivalAirport, departureDate, returnDate);
        }
    }
}
