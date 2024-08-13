package ru.weather.bot.weatherbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

@Service
public class StackMessages
{
    private final List<SendMessage> messages;

    private int top;

    public StackMessages()
    {
        this.messages = new ArrayList<>();
        top = -1;
    }

    public boolean push(SendMessage message)
    {
        messages.add(++top, message);
        return true;
    }

    public SendMessage pop()
    {
        if (messages.isEmpty())
            throw new EmptyStackException();
        return messages.get(top--);
    }

    public int size()
    {
        return messages.size();
    }

    public boolean isEmpty()
    {
        return messages.isEmpty();
    }
}