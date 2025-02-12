package com.nebarrow.weathertracker.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinate(@JsonProperty("lon") double longitude,
                         @JsonProperty("lat") double latitude) {
}
