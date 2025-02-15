package com.nebarrow;

import com.nebarrow.weathertracker.dto.response.WeatherResponseByCoordinate;
import com.nebarrow.weathertracker.dto.response.WeatherResponseByName;
import com.nebarrow.weathertracker.exception.OpenWeatherApiException;
import com.nebarrow.weathertracker.model.common.Coordinate;
import com.nebarrow.weathertracker.service.WeatherService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = TestConfiguration.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebAppConfiguration
@Transactional
@ActiveProfiles("application-test")
public class OpenWeatherApiServiceTest {
    @BeforeEach
    public void setUp() {
        Mockito.reset(openWeatherApiService);
    }

    @Mock
    private WeatherService openWeatherApiService;

    @Test
    public void WeatherService_GetWeatherByCityName_ShouldReturnWeather() {
        var weatherApiAnswer = WeatherResponseByName.builder()
                        .name("London")
                        .state("England")
                        .country("UK")
                        .latitude("51.51")
                        .longitude("0.13")
                        .isAdded(false)
                        .build();

        Mockito.doReturn(List.of(weatherApiAnswer)).when(openWeatherApiService).getWeatherByName("London");
        var weather = openWeatherApiService.getWeatherByName("London");
        assertEquals(weather.getFirst(), weatherApiAnswer);
    }

    @Test
    public void WeatherService_GetWeatherByCityCoordinate_ShouldReturnWeather() {
        var weatherApiByNameAnswer = new WeatherResponseByName[]{WeatherResponseByName.builder()
                .name("London")
                .state("England")
                .country("UK")
                .latitude("51.51")
                .longitude("0.13")
                .isAdded(false)
                .build(),
                WeatherResponseByName.builder()
                        .name("London")
                        .state("NotEngland")
                        .country("NotUK")
                        .latitude("2.35")
                        .longitude("48.85")
                        .isAdded(false)
                        .build()};
        var weatherApiByCoordinateAnswer = WeatherResponseByCoordinate.builder()
                .name("London")
                .state("NotEngland")
                .coordinate(new Coordinate(48.85, 2.35))
                .build();

        Mockito.doReturn(List.of(weatherApiByNameAnswer)).when(openWeatherApiService).getWeatherByName("London");
        Mockito.doReturn(weatherApiByCoordinateAnswer).when(openWeatherApiService).getWeatherByCoordinate(48.85, 2.35);
        var givenWeather = openWeatherApiService.getWeatherByName("London");
        var weather = openWeatherApiService.getWeatherByCoordinate(Double.parseDouble(givenWeather.get(1).longitude()), Double.parseDouble(givenWeather.get(1).latitude()));
        assertEquals(weather, weatherApiByCoordinateAnswer);
    }

    @Test
    public void WeatherService_TryGetWeatherByEmptyCityName_ShouldThrowException() {
        Mockito.doThrow(new OpenWeatherApiException("Bad Request: Invalid City Name")).when(openWeatherApiService).getWeatherByName("");
        try {
            openWeatherApiService.getWeatherByName("");
        } catch (OpenWeatherApiException e) {
            assertEquals("Bad Request: Invalid City Name", e.getMessage());
        }
    }
}
