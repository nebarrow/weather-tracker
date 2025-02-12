package com.nebarrow.weathertracker.repository;

import com.nebarrow.weathertracker.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByUserId(int userId);

    Location findByUserIdAndLatitudeAndLongitude(int userId, double latitude, double longitude);
}
