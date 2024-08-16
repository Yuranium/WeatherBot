package ru.weather.bot.weatherbot.service;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageSend
{
    public static SendMessage createMessage(long chatId, String text)
    {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.HTML);
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }
}