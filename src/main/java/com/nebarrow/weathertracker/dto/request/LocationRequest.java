package com.nebarrow.weathertracker.dto.request;

public record LocationRequest(String name, int userId, Double latitude, Double longitude) {
}
