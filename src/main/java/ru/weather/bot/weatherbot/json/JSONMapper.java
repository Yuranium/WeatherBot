package ru.weather.bot.weatherbot.json;

import org.springframework.beans.factory.annotation.Value;

public class JSONMapper
{
    @Value("${weather.api.key}")
    private String weatherAPIKey;

    @Value("${url.template}")
    private String templateURL;
    private JSONWeather weather;

    public boolean isCity(String text)
    {
        return false;
    }
}