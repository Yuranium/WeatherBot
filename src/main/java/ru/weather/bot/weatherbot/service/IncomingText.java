package ru.weather.bot.weatherbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.BotModel;
import ru.weather.bot.weatherbot.models.Messages;

import java.io.File;
import java.util.Deque;

import static ru.weather.bot.weatherbot.service.MessageSend.createMessage;

//@Service
public class IncomingText implements IncomingData
{
    private final Deque<BotApiMethod<?>> stackMessages;
    private BotLanguage botLanguage;
    private BotCommand botCommand;

    //@Autowired
    public IncomingText(Deque<BotApiMethod<?>> stackMessages)
    {
        this.stackMessages = stackMessages;
        botLanguage = BotLanguage.ENGLISH;
    }

    @Override
    public void sendler(Update update)
    {}
}