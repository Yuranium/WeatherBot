package ru.weather.bot.weatherbot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.Observer;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;

@Component
public class DefaultCommand implements CommandHandler, Observer
{
    private BotLanguage language;
    @Override
    public BotApiMethod<?> processCommand(Update update)
    {
        String message = switch (language)
        {
            case RUSSIAN -> Messages.RU_UNSUPPORTED_COMMAND;
            case ENGLISH -> Messages.EN_UNSUPPORTED_COMMAND;
            case CHINESE -> Messages.CN_UNSUPPORTED_COMMAND;
            case GERMAN -> Messages.DE_UNSUPPORTED_COMMAND;
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
        return BotCommand.DEFAULT;
    }

    @Override
    public void update(BotLanguage language)
    {
        this.language = language;
    }
}