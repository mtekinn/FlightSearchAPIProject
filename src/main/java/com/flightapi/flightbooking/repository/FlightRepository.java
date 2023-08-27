package com.flightapi.flightbooking.repository;

import com.flightapi.flightbooking.model.Airport;
import com.flightapi.flightbooking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureAirportAndArrivalAirportAndDepartureDateBetween(
            Airport departureAirport,
            Airport arrivalAirport,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
