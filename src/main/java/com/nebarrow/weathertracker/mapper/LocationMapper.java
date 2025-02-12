package com.nebarrow.weathertracker.mapper;

import com.nebarrow.weathertracker.dto.request.LocationRequest;
import com.nebarrow.weathertracker.dto.response.LocationResponse;
import com.nebarrow.weathertracker.model.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationResponse toDto(Location location);

    Location toEntity(LocationRequest weatherResponse);
}
