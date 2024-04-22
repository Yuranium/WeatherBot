package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.weather.bot.weatherbot.config.WeatherConfig;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Getter
@Component
public class WeatherMapper
{
    private final WeatherConfig weatherConfig;

    private WeatherData weatherData;

    @Autowired
    public WeatherMapper(WeatherConfig weatherConfig)
    {
        this.weatherConfig = weatherConfig;
    }

    public String weatherDispatch(String cityName, BotLanguage language)
    {
        weatherData = fetchWeather(cityName, language);

        return (weatherData == null) ? null : Messages.weatherForecast(language, convertCityNameCorrectly(cityName),
                weatherData.weather().get(0).description(), weatherData.main().temp(), weatherData.main().feels_like(),
                weatherData.clouds().clouds(), weatherData.wind().speed());
    }

    public String detailedWeatherForecast(String cityName, BotLanguage language)
    {
        return (weatherData == null) ? null : Messages.detailedWeatherForecast(language, convertCityNameCorrectly(cityName),
                weatherData.weather().get(0).description(), weatherData.main().temp(), weatherData.main().feels_like(), weatherData.main().temp_min(),
                weatherData.main().temp_max(), weatherData.main().pressure(), weatherData.main().humidity(), (weatherData.visibility() / 1000D),
                weatherData.wind().speed(), weatherData.wind().gust(), weatherData.wind().windGustFinding(language), weatherData.clouds().clouds());
    }

    private String convertCityNameCorrectly(String str)
    {
        if (str == null || str.isEmpty())
            throw new IllegalArgumentException("Invalid string 'str' passed in upperFirstChar(String str) method, string does not exist or it is empty");
        int index = str.indexOf("-");
        return str.contains("-") ? str.substring(0, 1).toUpperCase() + str.substring(1, index + 1) + str.substring(index + 1, index + 2).toUpperCase()
                + str.substring(index + 2) : str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private WeatherData fetchWeather(String city, BotLanguage language)
    {
        try
        {
            String urlString = weatherConfig.getTemplateUrl().replace("{city name}", city).replace("{my lang}", language.getRegion())
                    .replace("{units}", "metric").replace("{API key}", weatherConfig.getWeatherApiKey());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Получение ответа
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Чтение ответа
                StringBuilder response = new StringBuilder();
                try (Scanner scanner = new Scanner(connection.getInputStream()))
                {
                    while (scanner.hasNextLine())
                        response.append(scanner.nextLine());
                }

                // Преобразование JSON в объект WeatherData с помощью Jackson
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.toString(), WeatherData.class);
            } else
            {
                return null;
                // throw new RuntimeException("HTTP Response Code: " + responseCode);
            }
        } catch (IOException exc)
        {
            exc.printStackTrace();
        }
        return null;
    }
}