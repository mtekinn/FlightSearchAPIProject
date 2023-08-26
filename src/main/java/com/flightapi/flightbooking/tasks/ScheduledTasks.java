package com.flightapi.flightbooking.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 10000)
    public void getFlights() {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/flights", HttpMethod.GET, null, String.class);
        System.out.println(response.getBody());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
