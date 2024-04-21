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

    private WeatherData weatherData;

    @Autowired
    public WeatherMapper(WeatherConfig weatherConfig)
    {
        this.weatherConfig = weatherConfig;
    }

    public String weatherDispatch(String cityName, BotLanguage language)
    {
        weatherData = fetchWeather(cityName, language);

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
        return (weatherData == null) ? null : switch (language)
        {
            case RUSSIAN -> "\uD83C\uDFD9 Хорошо! Более <b>подробный прогноз погоды</b> в регионе <b>" + convertCityNameCorrectly(cityName) +
                    "</b> сейчас:\n\n\uD83C\uDF26 Описание: <b>" + weatherData.weather().get(0).description() + "</b>" +
                    "\n\n\uD83C\uDF21 Средняя температура: <b>" + weatherData.main().temp() + "°C</b>" +
                    "\n\n\uD83D\uDC64 Ощущается как: <b>" + weatherData.main().feels_like() + "°C</b>" +
                    "\n\n\uD83E\uDDCA Минимальная температура: <b>" + weatherData.main().temp_min() + "°C</b>" +
                    "\n\n\uD83D\uDD25 Максимальная температура: <b>" + weatherData.main().temp_max() + "°C</b>" +
                    "\n\n\uD83D\uDDDC Давление: <b>" + weatherData.main().pressure() + " мм рт. ст.</b>" +
                    "\n\n\uD83D\uDCA6 Влажность: <b>" + weatherData.main().humidity() + "%</b>" +
                    "\n\n\uD83D\uDD2D Видимость: <b>" + (weatherData.visibility() / 1000D) + "км</b>" +
                    "\n\n\uD83D\uDCA8 Скорость ветра: <b>" + weatherData.wind().speed() + "м/c</b>" +
                    "\n\n\uD83C\uDF2C Порывы ветра (кратковременное усиление ветра): <b>" + weatherData.wind().gust() + "м/с</b>" +
                    "\n\n\uD83E\uDDED Направление ветра: <b>" + weatherData.wind().windGustFinding(language) + "</b>" +
                    "\n\n☁ Облачность неба: <b>" + weatherData.clouds().clouds() + "%</b>";
            case ENGLISH -> "\uD83C\uDFD9 Good! More <b>detailed weather forecast</b> for the region <b>" + convertCityNameCorrectly(cityName) + "</b> now:" +
                    "\n\n\uD83C\uDF26 Description: <b>" + weatherData.weather().get(0).description() + "</b>" +
                    "\n\n\uD83C\uDF21 Average temperature: <b>" + weatherData.main().temp() + "°C</b>" +
                    "\n\n\uD83D\uDC64 Feels like: <b>" + weatherData.main().feels_like() + "°C</b>" +
                    "\n\n\uD83E\uDDCA Minimum temperature: <b>" + weatherData.main().temp_min() + "°C</b>" +
                    "\n\n\uD83D\uDD25 Maximum temperature: <b>" + weatherData.main().temp_max() + "°C</b>" +
                    "\n\n\uD83D\uDDDC Pressure: <b>" + weatherData.main().pressure() + " mmHg</b>" +
                    "\n\n\uD83D\uDCA6 Humidity: <b>" + weatherData.main().humidity() + "%</b>" +
                    "\n\n\uD83D\uDD2D Visibility: <b>" + (weatherData.visibility() / 1000D) + "km</b>" +
                    "\n\n\uD83D\uDCA8 Wind speed: <b>" + weatherData.wind().speed() + "m/sec</b>" +
                    "\n\n\uD83C\uDF2C Wind gusts (short-term wind strengthening): <b>" + weatherData.wind().gust() + "m/sec</b>" +
                    "\n\n\uD83E\uDDED Wind direction: <b>" + weatherData.wind().windGustFinding(language) + "</b>" +
                    "\n\n☁ Sky cloudiness: <b>" + weatherData.clouds().clouds() + "%</b>";
            case CHINESE -> "\uD83C\uDFD9 好! 该地区更详细的天气预报 <b>" + convertCityNameCorrectly(cityName) + "</b> 现在：" +
                    "\n\n\uD83C\uDF26 描述： <b>" + weatherData.weather().get(0).description() + "</b>" +
                    "\n\n\uD83C\uDF21 平均气温： <b>" + weatherData.main().temp() + "°C</b>" +
                    "\n\n\uD83D\uDC64 感觉： <b>" + weatherData.main().feels_like() + "°C</b>" +
                    "\n\n\uD83E\uDDCA 最低气温： <b>" + weatherData.main().temp_min() + "°C</b>" +
                    "\n\n\uD83D\uDD25 最高气温： <b>" + weatherData.main().temp_max() + "°C</b>" +
                    "\n\n\uD83D\uDDDC 气压： <b>" + weatherData.main().pressure() + " mmHg。</b>" +
                    "\n\n\uD83D\uDCA6 湿度： <b>" + weatherData.main().humidity() + "%</b>" +
                    "\n\n\uD83D\uDD2D 能见度： <b>" + (weatherData.visibility() / 1000D) + "公里</b>" +
                    "\n\n\uD83D\uDCA8 风速： <b>" + weatherData.wind().speed() + "米/秒。</b>" +
                    "\n\n\uD83C\uDF2C 阵风（短期风力增强）： <b>" + weatherData.wind().gust() + "米/秒。</b>" +
                    "\n\n\uD83E\uDDED 风向： <b>" + weatherData.wind().windGustFinding(language) + "</b>" +
                    "\n\n☁ 天空云量： <b>" + weatherData.clouds().clouds() + "%</b>";
            case GERMAN -> "\uD83C\uDFD9 Gut! <b>Ausführlichere Wettervorhersage</b> für die Region <b>" + convertCityNameCorrectly(cityName) + "</b> jetzt:" +
                    "\n\n\uD83C\uDF26 Beschreibung: <b>" + weatherData.weather().get(0).description() + "</b>" +
                    "\n\n\uD83C\uDF21 Durchschnittliche Temperatur: <b>" + weatherData.main().temp() + "°C</b>" +
                    "\n\n\uD83D\uDC64 Fühlt sich an wie: <b>" + weatherData.main().feels_like() + "°C</b>" +
                    "\n\n\uD83E\uDDCA Tiefsttemperatur: <b>" + weatherData.main().temp_min() + "°C</b>" +
                    "\n\n\uD83D\uDD25 Höchsttemperatur: <b>" + weatherData.main().temp_max() + "°C</b>" +
                    "\n\n\uD83D\uDDDC Luftdruck: <b>" + weatherData.main().pressure() + " mmHg</b>" +
                    "\n\n\uD83D\uDCA6 Luftfeuchtigkeit: <b>" + weatherData.main().humidity() + "%</b>" +
                    "\n\n\uD83D\uDD2D Sichtweite: <b>" + (weatherData.visibility() / 1000D) + "km</b>" +
                    "\n\n\uD83D\uDCA8 Windgeschwindigkeit: <b>" + weatherData.wind().speed() + "m/sec</b>" +
                    "\n\n\uD83C\uDF2C Windböen (kurzfristige Windverstärkung): <b>" + weatherData.wind().gust() + "m/sec</b>" +
                    "\n\n\uD83E\uDDED Windrichtung: <b>" + weatherData.wind().windGustFinding(language) + "</b>" +
                    "\n\n☁ Bewölkungsgrad: <b>" + weatherData.clouds().clouds() + "%</b>";
        };
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