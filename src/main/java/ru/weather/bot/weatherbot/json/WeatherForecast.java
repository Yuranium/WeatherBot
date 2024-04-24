package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherForecast(
        @JsonProperty("list")
        List<WeatherData> weatherDataList
)
{
    @Override
    public String toString() {
        return "WeatherForecast{" +
                "weatherDataList=" + weatherDataList +
                '}';
    }
}