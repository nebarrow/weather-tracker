package com.nebarrow.weathertracker.repository;

import com.nebarrow.weathertracker.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
