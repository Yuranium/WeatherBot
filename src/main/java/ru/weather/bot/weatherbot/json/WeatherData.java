package ru.weather.bot.weatherbot.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    public String windGustFinding()
    {
        if ((deg <= 360 && deg >= 350) || (deg <= 10 && deg >= 0))
            return "Северное направление ветра";
        if (deg <= 100 && deg >= 80)
            return "Восточное направление ветра";
        if (deg <= 190 && deg >= 170)
            return "Южное направление ветра";
        if (deg <= 280 && deg >= 260)
            return "Западное направление ветра";
        if (deg > 10 && deg < 80)
            return "Северо-восточное направление ветра";
        if (deg > 100 && deg < 170)
            return "Юго-восточное направление ветра";
        if (deg > 190 && deg < 260)
            return "Юго-западное направление ветра";
        if (deg > 280 && deg < 350)
            return "Северо-западное направление ветра";
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