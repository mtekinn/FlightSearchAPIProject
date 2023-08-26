package com.flightapi.flightbooking.service;

import com.flightapi.flightbooking.model.Airport;
import com.flightapi.flightbooking.model.Flight;
import com.flightapi.flightbooking.repository.AirportRepository;
import com.flightapi.flightbooking.repository.FlightRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;


@Service
public class FlightSchedulerService {

    private final FlightRepository flightRepository;

    private final AirportRepository airportRepository;

    private final RestTemplate restTemplate;

    public FlightSchedulerService(FlightRepository flightRepository, AirportRepository airportRepository, RestTemplate restTemplate) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 0 * * ?") // It will run at 00:00:00 every day
    public void updateFlightsFromApi() {
        ResponseEntity<Flight[]> response = restTemplate.getForEntity(
                "https://64e95f8299cf45b15fe09b9a.mockapi.io/api/flight",
                Flight[].class);

        Flight[] flightsFromApi = response.getBody();
        if (flightsFromApi != null) {
            for (Flight flightFromApi : flightsFromApi) {
                // If the flight is already in the database, update it
                Optional<Flight> matchingFlight = flightRepository.findById(flightFromApi.getId());
                if (matchingFlight.isPresent()) {
                    Flight flightToUpdate = matchingFlight.get();
                    // Flight's fields can be updated like this
                    flightToUpdate.setPrice(flightFromApi.getPrice());
                    flightToUpdate.setDepartureDate(flightFromApi.getDepartureDate());
                    flightRepository.save(flightToUpdate);
                } else {
                    // If the flight is not in the database, save it
                    flightRepository.save(flightFromApi);
                }
            }
        }
    }


    @Scheduled(cron = "0 0 1 * * ?") // It will run at 01:00:00 every day
    public void updateAirportsFromApi() {
        ResponseEntity<Airport[]> response = restTemplate.getForEntity(
                "https://64e95f8299cf45b15fe09b9a.mockapi.io/api/airports",
                Airport[].class);

        Airport[] airportsFromApi = response.getBody();
        if (airportsFromApi != null) {
            for (Airport airportFromApi : airportsFromApi) {
                // If the airport is already in the database, update it
                Optional<Airport> matchingAirport = airportRepository.findById(airportFromApi.getId());
                if (matchingAirport.isPresent()) {
                    Airport airportToUpdate = matchingAirport.get();
                    // Update the fields like this
                    airportToUpdate.setCity(airportFromApi.getCity());
                    airportRepository.save(airportToUpdate);
                } else {
                    // If the airport is not in the database, save it
                    airportRepository.save(airportFromApi);
                }
            }
        }
    }
}
