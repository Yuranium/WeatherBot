package ru.weather.bot.weatherbot.service;

import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.EmptyStackException;

@Component
public class StackMessages<T>
{
    private T[] messages;
    private int capacity;
    private  int top;

    public StackMessages(Class<T> clazz)
    {
        top = -1;
        capacity = 10;
        this.messages = (T[]) Array.newInstance(clazz, capacity);
    }

    public boolean push(T message)
    {
        if (top == messages.length - 1)
            throw new StackOverflowError("the message stack capacity is exceeded: the size is " + capacity + ", the inserted element is " + (top + 1));
        messages[++top] = message;
        return true;
    }

    public T pop()
    {
        if (top == -1)
            throw new EmptyStackException();
        return messages[top--];
    }

    public int size()
    {
        return capacity;
    }
}
