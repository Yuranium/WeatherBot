package ru.weather.bot.weatherbot.service.callback;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallBackHandler
{
    BotApiMethod<?> processCallBack(Update update);

    String getCallBackData();
}