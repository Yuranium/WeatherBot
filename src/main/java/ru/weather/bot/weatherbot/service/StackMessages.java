package ru.weather.bot.weatherbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.EmptyStackException;
import java.util.Iterator;

@Service
public class StackMessages implements Iterable<StackMessages> // Доработка
{
    private BotApiMethod<?>[] messages;
    private int capacity;
    private int top;

    public StackMessages()
    {
        top = -1;
        capacity = 10;
        this.messages = new BotApiMethod[capacity];
    }

    public boolean push(BotApiMethod<?> message)
    {
        if (top == messages.length - 1)
            throw new StackOverflowError("the message stack capacity is exceeded: the size is " + capacity + ", the inserted element is " + (top + 1));
        messages[++top] = message;
        return true;
    }

    public BotApiMethod<?> pop()
    {
        if (top == -1)
            throw new EmptyStackException();
        return messages[top--];
    }

    private void resize()
    {
        capacity *= 2;
        BotApiMethod<?>[] newMessage = new BotApiMethod[capacity];
        for (int i = size(); i >= 0; i--)
            newMessage[i] = pop();
        messages = newMessage; // Доработка
    }

    public int size()
    {
        return capacity;
    }

    public boolean isEmpty()
    {
        return capacity == 0;
    }

    @Override
    public Iterator<StackMessages> iterator() {
        return new Iterator<>()
        {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public StackMessages next() {
                return null;
            }
        };
    }
}