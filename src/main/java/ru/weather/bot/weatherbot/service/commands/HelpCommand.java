package ru.weather.bot.weatherbot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;

@Component
public class HelpCommand implements CommandHandler
{
    @Override
    public BotApiMethod<?> processCommand(Update update, BotLanguage language)
    {
        String message = switch (language)
        {
            case RUSSIAN -> Messages.RU_HELP;
            case ENGLISH -> Messages.EN_HELP;
            case CHINESE -> Messages.CN_HELP;
            case GERMAN -> Messages.DE_HELP;
        };
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(message)
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public BotCommand currentCommand()
    {
        return BotCommand.HELP;
    }
}