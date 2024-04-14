package ru.weather.bot.weatherbot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.weather.bot.weatherbot.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot
{
    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig)
    {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername()
    {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch ( message)
            {
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    defaultCommand(chatId);
            }
        }
    }

    public void startCommand(long chatId, String name)
    {
        String message = "Hi, " + name + ", glad to meet you \uD83D\uDC4B.\n\n" +
                "❗✋ Before you work with me further, choose the right language to communicate in. ✋❗";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try
        {
            execute(sendMessage);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void defaultCommand(long chatId)
    {
        String message = "Sorry, this command is not supported at the moment";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try
        {
            execute(sendMessage);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }
}