package ru.weather.bot.weatherbot.service.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;

public interface CommandHandler
{
    BotApiMethod<?> processCommand(Update update, BotLanguage language);
    BotCommand currentCommand();
}