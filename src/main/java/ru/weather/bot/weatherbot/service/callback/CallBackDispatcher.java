package ru.weather.bot.weatherbot.service.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.service.commands.DefaultCommand;

import java.util.List;
import java.util.Optional;

@Service
public class CallBackDispatcher
{
    private final List<CallBackHandler> handlers;
    private final DefaultCommand command;

    @Autowired
    public CallBackDispatcher(List<CallBackHandler> handlers, DefaultCommand command)
    {
        this.handlers = handlers;
        this.command = command;
    }

    public BotApiMethod<?> currentCallBack(Update update)
    {
        String callBackData = update.getCallbackQuery().getData();
        Optional<CallBackHandler> handler = handlers.stream()
                .filter(callBack -> callBack.getCallBackData().equals(callBackData))
                .findAny();
        if (handler.isEmpty())
            return command.processCommand(update);
        else return handler.get().processCallBack(update);
    }
}