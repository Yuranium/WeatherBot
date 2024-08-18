package ru.weather.bot.weatherbot.service.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.weather.bot.weatherbot.Observer;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.models.BotModel;

@Component
public class StartCommand implements CommandHandler, Observer
{
    @Override
    public BotApiMethod<?> processCommand(Update update)
    {
        String message = "Hi, " + update.getMessage().getChat().getFirstName() + ", glad to meet you \uD83D\uDC4B\n\n" +
                "❗✋ Before you work with me further, choose the right language to communicate in. ✋❗";
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(message)
                .replyMarkup(new InlineKeyboardMarkup(BotModel.getRowsForScreenButton()))
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public BotCommand currentCommand()
    {
        return BotCommand.START;
    }

    @Override
    public void update(BotLanguage language) {}
}