package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.weather.bot.weatherbot.config.WeatherConfig;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;


import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Getter
@Component
public class WeatherMapper
{
    private final ReceiveData receiveData;

    private final ProcessingData processingData;

    private WeatherData weatherData;

    private WeatherForecast weatherForecast;

    @Autowired
    public WeatherMapper(ReceiveData receiveData, ProcessingData processingData)
    {
        this.receiveData = receiveData;
        this.processingData = processingData;
    }

    public String weatherDispatch(String cityName, BotLanguage language)
    {
        weatherData = receiveData.fetchWeather(cityName, language);

        return (weatherData == null) ? null : Messages.weatherForecast(language, processingData.convertCityNameCorrectly(cityName),
                weatherData.weather().get(0).description(), weatherData.main().temp(), weatherData.main().feels_like(),
                weatherData.clouds().clouds(), weatherData.wind().speed());
    }

    public String detailedWeather(String cityName, BotLanguage language)
    {
        return (weatherData == null) ? null : Messages.detailedWeatherForecast(language, processingData.convertCityNameCorrectly(cityName),
                weatherData.weather().get(0).description(), weatherData.main().temp(), weatherData.main().feels_like(), weatherData.main().temp_min(),
                weatherData.main().temp_max(), weatherData.main().pressure(), weatherData.main().humidity(), weatherData.visibility(),
                weatherData.wind().speed(), weatherData.wind().gust(), weatherData.wind().windGustFinding(language), weatherData.clouds().clouds());
    }

    public String weatherForecastDispatch(String cityName, BotLanguage language)
    {
        weatherForecast = receiveData.fetchWeatherForecast(cityName, language);
        if (weatherForecast == null)
             return null;
        else
        {
            for (WeatherData data : weatherForecast.weatherDataList())
                System.out.println(data);
            String[] city_days = processingData.splitSpace(cityName);
            return Messages.weatherForecastGeneralInfo(language, processingData.convertCityNameCorrectly(city_days[0]), Integer.parseInt(city_days[1]));
        }
    }

    public String weatherForecastDay(String cityName, int position, BotLanguage language)
    {
        return (weatherForecast == null) ? null : Messages.weatherForecast(language, processingData.convertCityNameCorrectly(cityName),
                weatherForecast.weatherDataList().get(position - 1).weather().get(0).description(), weatherForecast.weatherDataList().get(position - 1).main().temp(),
                weatherForecast.weatherDataList().get(position - 1).main().feels_like(), weatherForecast.weatherDataList().get(position - 1).clouds().clouds(),
                weatherForecast.weatherDataList().get(position - 1).wind().speed());
    }

    public String detailedWeatherForecast(String cityName, int position, BotLanguage language)
    {
        return  (weatherForecast == null) ? null : Messages.detailedWeatherForecast(language, processingData.convertCityNameCorrectly(cityName),
                weatherForecast.weatherDataList().get(position - 1).weather().get(0).description(), weatherForecast.weatherDataList().get(position - 1).main().temp(),
                weatherForecast.weatherDataList().get(position - 1).main().feels_like(), weatherForecast.weatherDataList().get(position - 1).main().temp_min(),
                weatherForecast.weatherDataList().get(position - 1).main().temp_max(), weatherForecast.weatherDataList().get(position - 1).main().pressure(),
                weatherForecast.weatherDataList().get(position - 1).main().humidity(), weatherForecast.weatherDataList().get(position - 1).visibility(),
                weatherForecast.weatherDataList().get(position - 1).wind().speed(), weatherForecast.weatherDataList().get(position - 1).wind().gust(),
                weatherForecast.weatherDataList().get(position - 1).wind().windGustFinding(language), weatherForecast.weatherDataList().get(position - 1).clouds().clouds());
    }
}