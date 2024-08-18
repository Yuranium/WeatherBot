package ru.weather.bot.weatherbot;

import ru.weather.bot.weatherbot.enums.BotLanguage;

public interface Observer
{
    void update(BotLanguage language);
}