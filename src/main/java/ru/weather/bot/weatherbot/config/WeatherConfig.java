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

    private String cityName;

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }
}