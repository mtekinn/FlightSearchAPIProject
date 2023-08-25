package com.flightapi.flightbooking.service;

import com.flightapi.flightbooking.model.Airport;
import com.flightapi.flightbooking.model.Flight;
import com.flightapi.flightbooking.repository.AirportRepository;
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

    @Autowired
    private AirportRepository airportRepository;

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
        Airport departureAirport = airportRepository.findById(departureAirportId).orElseThrow(() -> new RuntimeException("Departure Airport not found"));
        Airport arrivalAirport = airportRepository.findById(arrivalAirportId).orElseThrow(() -> new RuntimeException("Arrival Airport not found"));

        if (returnDate == null) {
            return flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateAndReturnDateIsNull(departureAirport, arrivalAirport, departureDate);
        } else {
            List<Flight> departFlights = flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateBetween(departureAirport, arrivalAirport, departureDate, departureDate);
            List<Flight> returnFlights = flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateBetween(arrivalAirport, departureAirport, returnDate, returnDate);
            departFlights.addAll(returnFlights);
            return departFlights;
        }
    }
}
