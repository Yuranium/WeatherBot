package ru.weather.bot.weatherbot.service;


import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IncomingData
{
    BotApiMethod<?> sendler(Update update);
}
