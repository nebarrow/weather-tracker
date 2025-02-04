package com.nebarrow.weathertracker.repository;

import com.nebarrow.weathertracker.dto.response.LocationResponse;
import com.nebarrow.weathertracker.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByUserId(int userId);
}
