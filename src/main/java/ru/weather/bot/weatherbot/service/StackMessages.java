package ru.weather.bot.weatherbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

@Service
public class StackMessages
{
    private final List<BotApiMethod<?>> messages;

    private int top;

    public StackMessages()
    {
        this.messages = new ArrayList<>();
        top = -1;
    }

    public boolean push(BotApiMethod<?> message)
    {
        messages.add(++top, message);
        return true;
    }

    public BotApiMethod<?> pop()
    {
        if (messages.isEmpty())
            throw new EmptyStackException();
        return messages.get(top--);
    }

    public BotApiMethod<?> peek()
    {
        if (messages.isEmpty())
            throw new EmptyStackException();
        return messages.get(top);
    }

    public void clear()
    {
        messages.clear();
        top = -1;
    }

    public int size()
    {
        return top;
    }

    public boolean isEmpty()
    {
        return messages.isEmpty();
    }
}