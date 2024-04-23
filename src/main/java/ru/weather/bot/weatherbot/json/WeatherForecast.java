package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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