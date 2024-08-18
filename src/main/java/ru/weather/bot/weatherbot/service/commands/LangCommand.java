package ru.weather.bot.weatherbot.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.Observer;
import ru.weather.bot.weatherbot.Subject;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;

import java.util.List;

@Component
public class LangCommand implements CommandHandler, Observer, Subject
{
    private BotLanguage language;
    private final List<Observer> observers;

    @Autowired
    public LangCommand(List<Observer> observers)
    {
        this.observers = observers;
        this.language = BotLanguage.ENGLISH;
    }

    @Override
    public BotApiMethod<?> processCommand(Update update)
    {
        String message = switch (language)
        {
            case RUSSIAN -> Messages.SET_LANG_RU;
            case ENGLISH -> Messages.SET_LANG_EN;
            case CHINESE -> Messages.SET_LANG_CN;
            case GERMAN -> Messages.SET_LANG_DE;
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
        return BotCommand.LANG;
    }

    @Override
    public void update(BotLanguage language)
    {
        this.language = language;
    }

    @Override
    public void notifyObservers()
    {
        observers.forEach(obs -> obs.update(language));
    }
}