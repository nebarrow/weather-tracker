package com.nebarrow;

import com.nebarrow.weathertracker.dto.response.WeatherResponseByCoordinate;
import com.nebarrow.weathertracker.exception.OpenWeatherApiException;
import com.nebarrow.weathertracker.service.WeatherService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ContextConfiguration(classes = TestConfiguration.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebAppConfiguration
@Transactional
@ActiveProfiles("application-test")
public class OpenWeatherApiServiceTest {
    @Autowired
    private WebClient webClient;

    private MockServerClient mockServerClient;

    private WeatherService openWeatherApiService;

    @BeforeEach
    public void setUp() {
        ClientAndServer mockServer = ClientAndServer.startClientAndServer(1082);
        mockServerClient = new MockServerClient("localhost", mockServer.getPort());
        openWeatherApiService = new WeatherService(webClient);
    }

    @AfterEach
    public void tearDown() {
        if (mockServerClient != null) {
            mockServerClient.close();
        }
    }


    @Test
    public void WeatherService_GetWeatherByCityName_ShouldReturnWeather() {
        mockServerClient.when(request()
                .withMethod("GET")
                .withPath("/geo/1.0/direct")
                .withQueryStringParameter("q", "London")
                .withQueryStringParameter("limit", "5"))
                .respond(response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\":\"London\",\"lat\":\"51.51\",\"lon\":\"0.13\",\"country\":\"UK\",\"state\":\"England\"}]"));

        var apiResponse = openWeatherApiService.getWeatherByName("London");

        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.size());
        assertEquals("London", apiResponse.getFirst().name());
        assertEquals("51.51", apiResponse.getFirst().latitude());
        assertEquals("0.13", apiResponse.getFirst().longitude());
        assertEquals("UK", apiResponse.getFirst().country());

    }

    @Test
    public void WeatherService_GetWeatherByCityCoordinate_ShouldReturnWeather() {
        mockServerClient.when(request()
                        .withMethod("GET")
                        .withPath("/geo/1.0/direct")
                        .withQueryStringParameter("q", "London")
                        .withQueryStringParameter("limit", "5"))
                .respond(response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("["
                                  + "{\"name\":\"London\",\"lat\":\"51.51\",\"lon\":\"0.13\",\"country\":\"UK\",\"state\":\"England\"},"
                                  + "{\"name\":\"London\",\"lat\":\"48.85\",\"lon\":\"2.35\",\"country\":\"France\",\"state\":\"Ile-de-France\"},"
                                  + "{\"name\":\"London\",\"lat\":\"40.7128\",\"lon\":\"-74.0060\",\"country\":\"USA\",\"state\":\"New York\"},"
                                  + "{\"name\":\"London\",\"lat\":\"34.0522\",\"lon\":\"-118.2437\",\"country\":\"USA\",\"state\":\"California\"},"
                                  + "{\"name\":\"London\",\"lat\":\"51.45\",\"lon\":\"-0.25\",\"country\":\"UK\",\"state\":\"London\"}"
                                  + "]"));
        mockServerClient.when(request()
                .withMethod("GET")
                .withPath("/data/2.5/weather")
                .withQueryStringParameter("lat", "48.85")
                .withQueryStringParameter("lon", "2.35"))
                .respond(response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\":\"London\",\"coord\":{\"lat\":48.85,\"lon\":2.35},\"sys\":{\"country\":\"France\"},\"state\":\"Ile-de-France\"}, \"main\":{\"temp\":280.32,\"feels_like\":271.25,\"humidity\":74}, \"weather\": [{\"main\":\"Drizzle\",\"icon\":\"09d\"}]"));


        var apiByNameResponse = openWeatherApiService.getWeatherByName("London");
        WeatherResponseByCoordinate apiByCoordinateResponse = openWeatherApiService.getWeatherByCoordinate(
                Double.parseDouble(apiByNameResponse.get(1).longitude()),
                Double.parseDouble(apiByNameResponse.get(1).latitude()));

        assertNotNull(apiByCoordinateResponse);
        assertEquals("London", apiByCoordinateResponse.getName());
        assertEquals(48.85, apiByCoordinateResponse.getCoordinate().latitude());
        assertEquals(2.35, apiByCoordinateResponse.getCoordinate().longitude());

    }

    @Test
    public void WeatherService_TryGetWeatherWhenServerNotAvailable_ShouldThrowException() {
        mockServerClient.when(request()
                .withMethod("GET")
                .withPath("/geo/1.0/direct")
                .withQueryStringParameter("q", " ")
                .withQueryStringParameter("limit", "5"))
                .respond(response()
                        .withStatusCode(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]"));

        try {
            openWeatherApiService.getWeatherByName("London");
        } catch (OpenWeatherApiException e) {
            assertEquals("OpenWeather API is not available", e.getMessage());
        }
    }
}
