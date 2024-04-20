package ru.weather.bot.weatherbot.models;

import org.springframework.beans.factory.annotation.Autowired;
import ru.weather.bot.weatherbot.json.WeatherData;
import ru.weather.bot.weatherbot.json.WeatherMapper;

public class WeatherModel
{
    private WeatherMapper mapper;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WeatherModel(WeatherMapper mapper)
    {
        this.mapper = mapper;
    }

    public void weatherDispatch(String cityName)
    {
        WeatherData weatherData = mapper.fetchWeather(cityName);
    }
}