package ru.weather.bot.weatherbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.weather.bot.weatherbot.Observer;
import ru.weather.bot.weatherbot.config.BotConfig;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.Messages;
import ru.weather.bot.weatherbot.service.callback.CallBackDispatcher;

@Service
public class IncomingCallBack implements IncomingData, Observer
{
    private final CallBackDispatcher dispatcher;
    private final BotConfig botConfig;
    private BotLanguage language;

    public IncomingCallBack(CallBackDispatcher dispatcher, BotConfig botConfig)
    {
        this.dispatcher = dispatcher;
        this.botConfig = botConfig;
    }

    @Override
    public BotApiMethod<?> sendler(Update update)
    {
        if (botConfig.getMessageId() == null || (update.getMessage().getMessageId().intValue() != botConfig.getMessageId().intValue()))
        {
            SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(switch (language)
                    {
                        case RUSSIAN -> Messages.RU_UNSUCCESSFUL_EVENT_HANDLING;
                        case ENGLISH -> Messages.EN_UNSUCCESSFUL_EVENT_HANDLING;
                        case CHINESE -> Messages.CN_UNSUCCESSFUL_EVENT_HANDLING;
                        case GERMAN -> Messages.DE_UNSUCCESSFUL_EVENT_HANDLING;
                    })
                    .parseMode(ParseMode.HTML)
                    .build();
        }
        return dispatcher.currentCallBack(update);
    }

    @Override
    public void update(BotLanguage language)
    {
        this.language = language;
    }
}