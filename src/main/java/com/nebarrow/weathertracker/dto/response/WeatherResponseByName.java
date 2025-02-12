package com.nebarrow.weathertracker.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponseByName(@JsonProperty("name") String name,
                                    @JsonProperty("lat") String latitude,
                                    @JsonProperty("lon") String longitude,
                                    @JsonProperty("country") String country,
                                    @JsonProperty("state") String state,
                                    boolean isAdded) {
}
