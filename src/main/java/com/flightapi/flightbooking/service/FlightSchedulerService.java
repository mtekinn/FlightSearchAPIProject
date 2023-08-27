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

    @Scheduled(cron = "0 * * * * ?") // It will run at 02:00:00 every day
    public void updateAirportsFromApi() {
        try {
            ResponseEntity<Airport[]> response = restTemplate.getForEntity(
                    "https://64e95f8299cf45b15fe09b9a.mockapi.io/api/airports",
                    Airport[].class);

            Airport[] airportsFromApi = response.getBody();
            if (airportsFromApi != null) {
                for (Airport airportFromApi : airportsFromApi) {
                    Long airportId = airportFromApi.getId();
                    Optional<Airport> matchingAirport = airportRepository.findById(airportId);
                    if (matchingAirport.isPresent()) {
                        Airport airportToUpdate = matchingAirport.get();
                        airportToUpdate.setCity(airportFromApi.getCity());
                        airportRepository.save(airportToUpdate);
                        System.out.println("Havaalanı ID: " + airportId + " güncellendi.");
                    } else {
                        airportRepository.save(airportFromApi);
                        System.out.println("Havaalanı ID: " + airportId + " kaydedildi.");
                    }
                }
            } else {
                System.out.println("API'den havaalanı verisi alınamadı.");
            }
        } catch (Exception e) {
            System.out.println("Havaalanı verilerini güncellerken bir hata oluştu: " + e.getMessage());
        }
    }


    @Scheduled(cron = "0 * * * * ?") // It will run at 02:00:00 every day
    public void updateFlightsFromApi() {
        try {
            ResponseEntity<Flight[]> response = restTemplate.getForEntity(
                    "https://64e95f8299cf45b15fe09b9a.mockapi.io/api/flights",
                    Flight[].class);

            Flight[] flightsFromApi = response.getBody();
            if (flightsFromApi != null) {
                for (Flight flightFromApi : flightsFromApi) {
                    Long departureAirportId = flightFromApi.getDepartureAirport().getId();
                    Long arrivalAirportId = flightFromApi.getArrivalAirport().getId();

                    Airport departureAirport = airportRepository.findById(departureAirportId).orElse(null);
                    Airport arrivalAirport = airportRepository.findById(arrivalAirportId).orElse(null);

                    if (departureAirport == null || arrivalAirport == null) {
                        System.out.println("Bir veya her iki havaalanı bulunamadı. Uçuş ID: " + flightFromApi.getId());
                        continue;
                    }

                    flightFromApi.setDepartureAirport(departureAirport);
                    flightFromApi.setArrivalAirport(arrivalAirport);

                    // If the flight is already in the database, update it
                    Optional<Flight> matchingFlight = flightRepository.findById(flightFromApi.getId());
                    if (matchingFlight.isPresent()) {
                        Flight flightToUpdate = matchingFlight.get();
                        flightToUpdate.setPrice(flightFromApi.getPrice());
                        flightToUpdate.setDepartureDate(flightFromApi.getDepartureDate());
                        flightToUpdate.setReturnDate(flightFromApi.getReturnDate());
                        flightToUpdate.setDepartureAirport(departureAirport);
                        flightToUpdate.setArrivalAirport(arrivalAirport);
                        flightRepository.save(flightToUpdate);
                    } else {
                        // If the flight is not in the database, save it
                        flightRepository.save(flightFromApi);
                    }
                }
            } else {
                System.out.println("API'den uçuş verisi alınamadı.");
            }
        } catch (Exception e) {
            System.out.println("Uçuş verilerini güncellerken bir hata oluştu: " + e.getMessage());
        }
    }
}
