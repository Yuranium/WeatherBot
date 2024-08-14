package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.weather.bot.weatherbot.config.WeatherConfig;
import ru.weather.bot.weatherbot.enums.BotLanguage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class ReceiveData
{
    @Getter
    private final WeatherConfig weatherConfig;

    private final ProcessingData processingData;

    private static final int MAX_DAYS = 5;

    private static final int MIN_DAYS = 0;

    private static final int ZOOM = 4;

    @Autowired
    public ReceiveData(WeatherConfig weatherConfig, ProcessingData processingData)
    {
        this.weatherConfig = weatherConfig;
        this.processingData = processingData;
    }

    public WeatherData fetchWeather(String city, BotLanguage language)
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

    public WeatherForecast fetchWeatherForecast(String city, BotLanguage language)
    {
        try
        {
            String[] city_days = processingData.splitSpace(city);
            int countDays = Integer.parseInt(city_days[1]);
            if (countDays > MAX_DAYS || countDays <= MIN_DAYS)
                return null;
            String urlString = weatherConfig.getForecastTemplateUrl().replace("{city name}", city_days[0])
                    .replace("{my lang}", language.getRegion()).replace("{units}", "metric")
                    .replace("{count day}", (countDays * 8) + "").replace("{API key}", weatherConfig.getWeatherApiKey());
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

    public File fetchWeatherMap(String city, BotLanguage language)
    {
        WeatherData data = fetchWeather(city, language);
        if (data != null)
        {
            try
            {
                int[] coord = processingData.weatherCoord(data.coord().latitude(), data.coord().longitude());
                String urlString = weatherConfig.getMapTemplateUrl().replace("{layer}", "temp_new").replace("{z}", ZOOM + "")
                        .replace("{x}", coord[0] + "").replace("{y}", coord[1] + "").replace("{API key}", weatherConfig.getWeatherApiKey());
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Получение ответа
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    File imageFile = new File("temp.png"); // todo менять путь при работе из docker: "/app/images/temp.png" и из самой IDE: "temp.png"
                    OutputStream outputStream = new FileOutputStream(imageFile);
                    InputStream inputStream = connection.getInputStream();

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1)
                        outputStream.write(buffer, 0, bytesRead);
                    inputStream.close();
                    outputStream.close();

                    return imageFile;
                } else
                {
                    return null;
                }
            } catch (IOException exc)
            {
                exc.printStackTrace();
            }
        }
        return null;
    }
}