package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JSONMapper
{
    @Value("${weather.api.key}")
    private String WEATHER_API_KEY;

    @Value("${url.template}")
    private String TEMPLATE_URL;
    private WeatherData weatherData;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try (scanner)
        {
            System.out.println("Введите город, погоду которого хотите узнать:");
            String city = scanner.nextLine();
            WeatherData weatherData;
            weatherData = fetchWeather(city);
            System.out.println("В городе " + convertCityNameCorrectly(city) + " " + weatherData.weather().get(0).description() + ", средняя температура на улице "
                    + weatherData.main().temp() + "°C, ощущается как " + weatherData.main().feels_like() +
                    "°C, облачность составляет " + weatherData.clouds().clouds() + "%, порывы ветра достигают " + weatherData.wind().speed() + " м/c.");
            System.out.println(weatherData);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String convertCityNameCorrectly(String str)
    {
        if (str == null || str.isEmpty())
            throw new IllegalArgumentException("Invalid string 'str' passed in upperFirstChar(String str) method, string does not exist or it is empty");
        int index = str.indexOf("-");
        return str.contains("-") ? str.substring(0, 1).toUpperCase() + str.substring(1, index + 1) + str.substring(index + 1, index + 2).toUpperCase()
                + str.substring(index + 2) : str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private WeatherData fetchWeather(String city) throws IOException {
        String urlString = TEMPLATE_URL.replace("{city name}", city).replace("{my lang}", "ru")
                .replace("{units}", "metric").replace("{API key}", WEATHER_API_KEY);
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
        }
        else throw new RuntimeException("HTTP Response Code: " + responseCode);
    }
}