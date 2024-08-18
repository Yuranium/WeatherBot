package ru.weather.bot.weatherbot.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;

import java.util.List;
import java.util.Optional;

@Service
public class CommandDispatcher
{
    private final List<CommandHandler> commandHandlers;
    private final DefaultCommand defaultCommand;

    @Autowired
    public CommandDispatcher(List<CommandHandler> commandHandlers, DefaultCommand defaultCommand)
    {
        this.commandHandlers = commandHandlers;
        this.defaultCommand = defaultCommand;
    }

    public BotApiMethod<?> currentCommand(Update update, BotLanguage language)
    {
        String text = update.getMessage().getText();
        if (text.startsWith("/"))
        {
            Optional<CommandHandler> currentCommand = commandHandlers.stream()
                    .filter(command -> command.currentCommand().getCommand().equals(text))
                    .findAny();
            if (currentCommand.isEmpty())
                return defaultCommand.processCommand(update, language);
            else return currentCommand.get().processCommand(update, language);
        }
        else return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(switch (language)
                        {
                            case RUSSIAN -> Messages.RU_CITY_INPUT_ERROR;
                            case ENGLISH -> Messages.EN_CITY_INPUT_ERROR;
                            case CHINESE -> Messages.CN_CITY_INPUT_ERROR;
                            case GERMAN -> Messages.DE_CITY_INPUT_ERROR;
                        })
                .parseMode(ParseMode.HTML)
                .build();
    }
}