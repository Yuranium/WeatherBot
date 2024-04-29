package ru.weather.bot.weatherbot.service;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class StackMessages<T>
{
    private final List<T> messages;

    public StackMessages()
    {
        this.messages = new LinkedList<>();
    }

    public boolean push(T message)
    {
        messages.add(message);
        return true;
    }

    public T pop()
    {
        return messages.get(messages.size() - 1);
    }
}
