package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.request.LocationRequest;
import com.nebarrow.weathertracker.dto.response.LocationResponse;
import com.nebarrow.weathertracker.dto.response.WeatherResponseByCoordinate;
import com.nebarrow.weathertracker.dto.response.WeatherResponseByName;
import com.nebarrow.weathertracker.exception.LocationAlreadyExistsException;
import com.nebarrow.weathertracker.exception.LocationNotFoundException;
import com.nebarrow.weathertracker.mapper.LocationMapper;
import com.nebarrow.weathertracker.repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final WeatherService weatherService;
    private final LocationMapper locationMapper;

    public List<LocationResponse> findByUserId(int userId) {
        return locationRepository.findByUserId(userId).stream().map(locationMapper::toDto).toList();
    }

    @Transactional
    public void save(LocationRequest locationRequest) {
        try {
            locationRepository.save(locationMapper.toEntity(locationRequest));
        } catch (DataIntegrityViolationException e) {
            throw new LocationAlreadyExistsException("Location with name " + locationRequest.name() + " already added");
        }
    }

    @Transactional
    public void delete(LocationRequest locationRequest) {
        BigDecimal latRounded = roundToTwoDecimals(locationRequest.latitude());
        BigDecimal lonRounded = roundToTwoDecimals(locationRequest.longitude());
        try {
            var location = locationRepository.findByUserIdAndLatitudeAndLongitude(locationRequest.userId(), latRounded.doubleValue(), lonRounded.doubleValue());
            locationRepository.delete(location);
        } catch (LocationNotFoundException e) {
            throw new LocationNotFoundException("Location with name " + locationRequest.name() + " not found");
        }
    }

    public List<WeatherResponseByCoordinate> getLocationWeatherByUserId(int userId) {
        var locations = findByUserId(userId);
        List<WeatherResponseByCoordinate> weathers = new ArrayList<>();
        if (!locations.isEmpty()) {
            for (var location : locations) {
                var weather = weatherService.getWeatherByCoordinate(location.longitude(), location.latitude());
                weather.setName(location.name());
                weathers.add(weather);
            }
        }
        return weathers;
    }

    public List<WeatherResponseByName> getWeatherByName(String name, int userId) {
        return weatherService.getWeatherByName(name).stream()
                .map(weather -> {
                    var location = new LocationRequest(name, userId, Double.parseDouble(weather.latitude()), Double.parseDouble(weather.longitude()));
                    boolean isAdded = isLocationAdded(location);
                    return new WeatherResponseByName(weather.name(), weather.latitude(), weather.longitude(), weather.country(), weather.state(), isAdded);
                }).toList();
    }

    public boolean isLocationAdded(LocationRequest locationRequest) {
        return locationRepository.findByUserIdAndLatitudeAndLongitude(locationRequest.userId(), locationRequest.latitude(), locationRequest.longitude()) != null;
    }

    private BigDecimal roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
