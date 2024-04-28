package ru.weather.bot.weatherbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("application.properties")
public class WeatherConfig
{
    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.url.template}")
    private String weatherTemplateUrl;

    @Value("${weather-forecast.url.template}")
    private String forecastTemplateUrl;

    @Value("${weather-map.url.template}")
    private String mapTemplateUrl;

    private String cityName;

    private String weatherMessage;

    private int quantityDays;

    private int currentDay; // Возможно удалить

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public void setWeatherMessage(String weatherMessage) {
        this.weatherMessage = weatherMessage;
    }

    public void setQuantityDays(int quantityDays)
    {
        this.quantityDays = quantityDays;
    }

    public void setCurrentDay(int currentDay)
    {
        this.currentDay = currentDay;
    }
}