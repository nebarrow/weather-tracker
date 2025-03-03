package com.nebarrow.weathertracker.dto.request;

public record DeleteLocationRequest(int userId, Double latitude, Double longitude) {
}
