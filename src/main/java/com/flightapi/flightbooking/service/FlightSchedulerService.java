package com.flightapi.flightbooking.service;

import com.flightapi.flightbooking.model.Airport;
import com.flightapi.flightbooking.model.Flight;
import com.flightapi.flightbooking.repository.AirportRepository;
import com.flightapi.flightbooking.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FlightSchedulerService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "0 0 0 * * ?") // It will run at 00:00:00 every day
    public void updateFlightsFromApi() {
        ResponseEntity<Flight[]> response = restTemplate.getForEntity(
                "https://64e95f8299cf45b15fe09b9a.mockapi.io/api/flight",
                Flight[].class);

        Flight[] flightsFromApi = response.getBody();

        if (flightsFromApi != null) {
            List<Flight> existingFlights = flightRepository.findAll();

            for (Flight flightFromApi : flightsFromApi) {
                // If the flight is already in the database, update it
                Optional<Flight> matchingFlight = existingFlights.stream().filter(f -> f.getId().equals(flightFromApi.getId())).findFirst();
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
            List<Airport> existingAirports = airportRepository.findAll();

            for (Airport airportFromApi : airportsFromApi) {
                // If the airport is already in the database, update it
                Optional<Airport> matchingAirport = existingAirports.stream().filter(a -> a.getId().equals(airportFromApi.getId())).findFirst();
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