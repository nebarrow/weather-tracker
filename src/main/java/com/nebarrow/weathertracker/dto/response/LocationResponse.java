package com.nebarrow.weathertracker.dto.response;

public record LocationResponse(String name, String userId, double latitude, double longitude) {
}
