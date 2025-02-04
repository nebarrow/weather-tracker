package com.nebarrow.weathertracker.mapper;

import com.nebarrow.weathertracker.dto.response.SessionResponse;
import com.nebarrow.weathertracker.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionResponse toDto(Session session);
}
