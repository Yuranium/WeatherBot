package ru.weather.bot.weatherbot.service.commands;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;
import java.util.Optional;

@Service
public class CommandDispatcher
{
    private final List<CommandHandler> commandHandlers;

    private final DefaultCommand defaultCommand;

    @Getter
    private String message;

    @Autowired
    public CommandDispatcher(List<CommandHandler> commandHandlers, DefaultCommand defaultCommand)
    {
        this.commandHandlers = commandHandlers;
        this.defaultCommand = defaultCommand;
    }

    public BotApiMethod<?> currentCommand(Update update)
    {
        message = update.getMessage().getText();
        Optional<CommandHandler> currentCommand = commandHandlers.stream()
                .filter(command -> command.currentCommand().getCommand().equals(message))
                .findAny();
        if (currentCommand.isEmpty())
            return defaultCommand.processCommand(update);
        else return currentCommand.get().processCommand(update);
    }

    public boolean isCommand(String message)
    {
        return message.startsWith("/") && !message.contains(" ");
    }
}