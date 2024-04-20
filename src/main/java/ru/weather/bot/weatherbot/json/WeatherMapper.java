package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.weather.bot.weatherbot.config.WeatherConfig;
import ru.weather.bot.weatherbot.enums.BotLanguage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Getter
@Component
public class WeatherMapper
{
    private final WeatherConfig weatherConfig;

    @Autowired
    public WeatherMapper(WeatherConfig weatherConfig)
    {
        this.weatherConfig = weatherConfig;
    }

    public String weatherDispatch(String cityName, BotLanguage language)
    {
        WeatherData weatherData = fetchWeather(cityName, language);
        System.out.println(weatherData);

        return (weatherData == null) ? null : switch (language)
        {
            case RUSSIAN -> "\uD83C\uDFD9\uFE0F В городе " + convertCityNameCorrectly(cityName) + " " + weatherData.weather().get(0).description() +
                    ", \uD83C\uDF21\uFE0F средняя температура на улице " + weatherData.main().temp() + "°C, ощущается как " + weatherData.main().feels_like() +
                    "°C, ☁ облачность составляет " + weatherData.clouds().clouds() + "%, \uD83D\uDCA8 порывы ветра достигают " + weatherData.wind().speed() + " м/c.";
            case ENGLISH -> "\uD83C\uDFD9\uFE0F In the city of " + convertCityNameCorrectly(cityName) + " " + weatherData.weather().get(0).description() +
                    ", \uD83C\uDF21\uFE0F the average temperature outside is " + weatherData.main().temp() + "°C, feels like " +  weatherData.main().feels_like() +
                    "°C, ☁ cloudiness is " + weatherData.clouds().clouds() + "%, \uD83D\uDCA8 wind gusts reach " + weatherData.wind().speed() + " m/s.";
            case CHINESE -> "\uD83C\uDFD9\uFE0F 在" + convertCityNameCorrectly(cityName) + weatherData.weather().get(0).description()
                    + "城市，\uD83C\uDF21\uFE0F，室外平均气温为 " + weatherData.main().temp() + "℃，感觉温度为" + weatherData.main().feels_like()
                    + "℃，☁ ，云量为 " + weatherData.clouds().clouds() + "，\uD83D\uDCA8 阵风达到 " + weatherData.wind().speed() + " 米/秒。";
            case GERMAN -> "\uD83C\uDFD9\uFE0F In der Stadt " + convertCityNameCorrectly(cityName) + " " +  weatherData.weather().get(0).description() +
                    ", \uD83C\uDF21\uFE0F beträgt die durchschnittliche Außentemperatur " + weatherData.main().temp() + "°C, gefühlt " +
                    weatherData.main().feels_like() + "°C, ☁ beträgt die Bewölkung " + weatherData.clouds().clouds() +
                    "%, \uD83D\uDCA8 die Windböen erreichen " + weatherData.wind().speed() + " m/s.";
        };
    }

    public String detailedWeatherForecast(String cityName, BotLanguage language)
    {
        WeatherData weatherData = fetchWeather(cityName, language);

        return weatherData.toString();
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
                //throw new RuntimeException("HTTP Response Code: " + responseCode); // Добавить возврат null сюда, если город не найден
            }
        } catch (IOException exc)
        {
            exc.printStackTrace();
        }
        return null;
    }
}