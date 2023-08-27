package com.flightapi.flightbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class FlightbookingApplication {
	public static void main(String[] args) {
		SpringApplication.run(FlightbookingApplication.class, args);
	}


}