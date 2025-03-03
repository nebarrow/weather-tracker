package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.response.WeatherResponseByCoordinate;
import com.nebarrow.weathertracker.dto.response.WeatherResponseByName;
import com.nebarrow.weathertracker.exception.OpenWeatherApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WebClient webClient;

    // TODO: используешь реактивный клиент, но блокируешь запрос
    public WeatherResponseByCoordinate getWeatherByCoordinate(double longitude, double latitude) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("data/2.5/weather")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new OpenWeatherApiException("OpenWeather API is not available")))
                .bodyToMono(WeatherResponseByCoordinate.class)
                .block();
    }

    public List<WeatherResponseByName> getWeatherByName(String name) {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String uri = UriComponentsBuilder.fromUriString("geo/1.0/direct")
                .queryParam("q", encodedName)
                .queryParam("limit", 5)
                .build()
                .toUri()
                .toString();

        return Objects.requireNonNull(webClient.get()
                        .uri(uri)
                        .retrieve()
                        .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new OpenWeatherApiException("OpenWeather API is not available")))
                        .bodyToMono(new ParameterizedTypeReference<List<WeatherResponseByName>>() {
                            })
                        .block());
    }
}