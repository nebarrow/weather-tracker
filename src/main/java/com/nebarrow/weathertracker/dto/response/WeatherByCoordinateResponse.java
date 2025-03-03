package com.nebarrow.weathertracker.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nebarrow.weathertracker.model.common.Coordinate;
import com.nebarrow.weathertracker.model.common.Description;
import com.nebarrow.weathertracker.model.common.Sys;
import com.nebarrow.weathertracker.model.common.Weather;
import lombok.*;

import java.util.List;
@Setter
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeatherByCoordinateResponse {
        @JsonProperty("name")
        private String name;

        @JsonProperty("sys")
        private Sys sys;

        @JsonProperty("state")
        private String state;

        @JsonProperty("weather")
        private List<Weather> weather;

        @JsonProperty("coord")
        private Coordinate coordinate;

        @JsonProperty("main")
        private Description description;
}
