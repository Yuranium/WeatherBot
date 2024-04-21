package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.weather.bot.weatherbot.enums.BotLanguage;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherData(
        List<Weather> weather,
        Main main,
        double visibility,
        Wind wind,
        Clouds clouds
)
{
    @Override
    public String toString()
    {
        return "WeatherData{" +
                "weather=" + weather +
                ", main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                '}';
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Weather(
        String main,
        String description
)
{
    @Override
    public String toString() {
        return "Weather{" +
                "main='" + main + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Main(
        double temp,
        double feels_like,
        double temp_min,
        double temp_max,
        double pressure,
        double humidity
)
{
    @Override
    public String toString() {
        return "Main{" +
                "temp=" + temp +
                ", feels_like=" + feels_like +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Wind(
        double speed,
        double deg,
        double gust
)
{
    public String windGustFinding(BotLanguage language)
    {
        if ((deg <= 360 && deg >= 350) || (deg <= 10 && deg >= 0))
            return switch (language)
            {
                case RUSSIAN -> "Северное";
                case ENGLISH -> "North";
                case CHINESE -> "北方向";
                case GERMAN -> "Norden";
            };
        if (deg <= 100 && deg >= 80)
            return switch (language)
            {
                case RUSSIAN -> "Восточное";
                case ENGLISH -> "Eastern";
                case CHINESE -> "东向";
                case GERMAN -> "Osten";
            };
        if (deg <= 190 && deg >= 170)
            return switch (language)
            {
                case RUSSIAN -> "Южное";
                case ENGLISH -> "South";
                case CHINESE -> "南向";
                case GERMAN -> "Süden";
            };
        if (deg <= 280 && deg >= 260)
            return switch (language)
            {
                case RUSSIAN -> "Западное";
                case ENGLISH -> "Western";
                case CHINESE -> "西部方向";
                case GERMAN -> "Richtung";
            };
        if (deg > 10 && deg < 80)
            return switch (language)
            {
                case RUSSIAN -> "Северо-восточное";
                case ENGLISH -> "Northeast";
                case CHINESE -> "西部方向";
                case GERMAN -> "Nord-Ost";
            };
        if (deg > 100 && deg < 170)
            return switch (language)
            {
                case RUSSIAN -> "Юго-восточное";
                case ENGLISH -> "Southeast";
                case CHINESE -> "西部方向";
                case GERMAN -> "Süd-Ost";
            };
        if (deg > 190 && deg < 260)
            return switch (language)
            {
                case RUSSIAN -> "Юго-западное";
                case ENGLISH -> "Southwest";
                case CHINESE -> "西部方向";
                case GERMAN -> "Südwesten";
            };
        if (deg > 280 && deg < 350)
            return switch (language)
            {
                case RUSSIAN -> "Северо-западное";
                case ENGLISH -> "Northwest";
                case CHINESE -> "西部方向";
                case GERMAN -> "Nordwesten";
            };
        else throw new IllegalArgumentException("Invalid value of the 'deg' variable. The range of this variable is {0, 360}");
    }

    @Override
    public String toString() {
        return "wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                ", gust=" + gust +
                '}';
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Clouds(
        @JsonProperty("all")
        int clouds
)
{
    @Override
    public String toString() {
        return "clouds{" +
                "all=" + clouds +
                '}';
    }
}