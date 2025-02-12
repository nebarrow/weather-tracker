package com.nebarrow.weathertracker.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Weather(@JsonProperty("main") String currentWeather,
                      @JsonProperty("icon") String icon) {
}
