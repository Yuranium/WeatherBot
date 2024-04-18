package ru.weather.bot.weatherbot.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JSONWeather
{
    private String city;
    private String description;
    private String atmosphericPrecipitation;
    private String CloudCover;
    private Double humidity;
    private Double rangeOfVisibility;
    private Double temperature;
    private Double pressure;
    private Double windPower;

    public JSONWeather(String city, String description, Double humidity)
    {
        this.city = city;
        this.description = description;
        this.humidity = humidity;
    }
}