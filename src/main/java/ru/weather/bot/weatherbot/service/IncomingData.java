package ru.weather.bot.weatherbot.service;


import org.telegram.telegrambots.meta.api.objects.Update;

public interface IncomingData
{
    void sendler(Update update);
}
