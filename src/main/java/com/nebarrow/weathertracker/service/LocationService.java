package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.response.LocationResponse;
import com.nebarrow.weathertracker.mapper.LocationMapper;
import com.nebarrow.weathertracker.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    public List<LocationResponse> findByUserId(int userId) {
        return locationRepository.findByUserId(userId).stream().map(locationMapper::toDto).toList();
    }
}
