package com.nebarrow.weathertracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherByCoordinateService {
    private final WebClient webClient;

    @Autowired
    public WeatherByCoordinateService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getWeatherByCoordinate(double longitude, double latitude) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getWeatherByName(String name) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", name)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
