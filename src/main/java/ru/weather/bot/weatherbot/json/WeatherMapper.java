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
import java.util.List;
import java.util.Scanner;

@Getter
@Component
public class WeatherMapper
{
    private final WeatherConfig weatherConfig;

    private WeatherData weatherData;

    private WeatherForecast weatherForecast;

    private static final int MAX_DAYS = 10;

    private static final int MIN_DAYS = 0;

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

    public String detailedWeather(String cityName, BotLanguage language)
    {
        return (weatherData == null) ? null : Messages.detailedWeatherForecast(language, convertCityNameCorrectly(cityName),
                weatherData.weather().get(0).description(), weatherData.main().temp(), weatherData.main().feels_like(), weatherData.main().temp_min(),
                weatherData.main().temp_max(), weatherData.main().pressure(), weatherData.main().humidity(), (weatherData.visibility() / 1000D),
                weatherData.wind().speed(), weatherData.wind().gust(), weatherData.wind().windGustFinding(language), weatherData.clouds().clouds());
    }

    public String weatherForecastDispatch(String cityName, BotLanguage language)
    {
        weatherForecast = fetchWeatherForecast(cityName, language);
        if (weatherForecast == null)
             return null;
        else
        {
            for (WeatherData data : weatherForecast.weatherDataList())
                System.out.println(data);
            return "Для тестирования: " + weatherForecast.weatherDataList().size();
        }
    }

    public String weatherForecastDay(String cityName, int position)
    {
        return cityName + " " + weatherForecast.weatherDataList().get(position - 1).toString();
    }

    private String convertCityNameCorrectly(String str)
    {
        if (str == null || str.isEmpty())
            throw new IllegalArgumentException("Invalid string 'str' passed in upperFirstChar(String str) method, string does not exist or it is empty");
        int index = str.indexOf("-");
        return str.contains("-") ? str.substring(0, 1).toUpperCase() + str.substring(1, index + 1) + str.substring(index + 1, index + 2).toUpperCase()
                + str.substring(index + 2) : str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public String[] splitSpace(String message)
    {
        if (message == null || message.isEmpty())
            return null;
        else
        {
            int cnt = 0;
            for (char ch : message.toCharArray())
                if (ch == ' ')
                    cnt++;
            if (cnt == 1)
                return message.split(" ");
            else
            {
                int index = 0;
                while (message.charAt(index) != ' ')
                    index++;
                String[] city_days = new String[2];
                city_days[0] = message.substring(0, index);
                city_days[1] = message.substring(message.lastIndexOf(' ') + 1);
                return city_days;
            }
        }
    }

    private WeatherData fetchWeather(String city, BotLanguage language)
    {
        try
        {
            String urlString = weatherConfig.getWeatherTemplateUrl().replace("{city name}", city).replace("{my lang}", language.getRegion())
                    .replace("{units}", "metric").replace("{API key}", weatherConfig.getWeatherApiKey());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Получение ответа
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
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

    private WeatherForecast fetchWeatherForecast(String city, BotLanguage language)
    {
        try
        {
            String[] city_days = splitSpace(city);
            if (Integer.parseInt(city_days[1]) > MAX_DAYS || Integer.parseInt(city_days[1]) <= MIN_DAYS)
                return null;
            String urlString = weatherConfig.getForecastTemplateUrl().replace("{city name}", city_days[0])
                    .replace("{my lang}", language.getRegion()).replace("{units}", "metric")
                    .replace("{count day}", city_days[1]).replace("{API key}", weatherConfig.getWeatherApiKey());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                StringBuilder response = new StringBuilder();
                try (Scanner scanner = new Scanner(connection.getInputStream()))
                {
                    while (scanner.hasNextLine())
                        response.append(scanner.nextLine());
                }
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.toString(), WeatherForecast.class);
            } else throw new RuntimeException("HTTP Response Code: " + responseCode);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
        return null;
    }
}