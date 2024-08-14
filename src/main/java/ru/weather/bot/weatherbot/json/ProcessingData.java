package ru.weather.bot.weatherbot.json;

import org.springframework.stereotype.Service;

@Service
public class ProcessingData
{
    private static final int ZOOM = 4;

    public String convertCityNameCorrectly(String str)
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

    public int[] weatherCoord(double lat, double lon)
    {
        var tileSize = 256;
        var numTiles = Math.pow(2, ZOOM);

        var sinLatitude = Math.sin(lat * Math.PI / 180);
        var pixelX = ((lon + 180) / 360) * tileSize * numTiles;
        var pixelY = (0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI)) * tileSize * numTiles;

        var tileX = Math.floor(pixelX / tileSize);
        var tileY = Math.floor(pixelY / tileSize);

        return new int[]{(int) tileX, (int) tileY};
    }
}