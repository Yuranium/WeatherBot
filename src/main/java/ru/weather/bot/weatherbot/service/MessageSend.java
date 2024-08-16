package ru.weather.bot.weatherbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.weather.bot.weatherbot.json.ReceiveData;

//@Service
public class MessageSend
{
    private final ReceiveData receiveData;

    //@Autowired
    public MessageSend(ReceiveData receiveData)
    {
        this.receiveData = receiveData;
    }

    public static SendMessage createMessage(long chatId, String text)
    {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.HTML);
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }
}