package com.nebarrow.weathertracker.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Description(@JsonProperty("temp") String temperature,
                          @JsonProperty("feels_like") String feelsLike,
                          @JsonProperty("humidity") String humidity) {
}
