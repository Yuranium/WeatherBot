package ru.weather.bot.weatherbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.weather.bot.weatherbot.models.BotModel;
import ru.weather.bot.weatherbot.models.Messages;
import ru.weather.bot.weatherbot.service.TelegramBot;

import static ru.weather.bot.weatherbot.service.MessageSend.createMessage;

@Getter
@AllArgsConstructor
public enum BotCommand
{
    START("/start"), HELP("/help"), LANG("/lang"), MAP("/map"), DEFAULT("");
    private final String command;

    public void startCommand(TelegramBot telegramBot, long chatId, String name)
    {
        String message = "Hi, " + name + ", glad to meet you \uD83D\uDC4B\n\n" +
                "❗✋ Before you work with me further, choose the right language to communicate in. ✋❗";
        telegramBot.executeMessage(createMessage(chatId, message), BotModel::getRowsForScreenButton);
    }

    public void helpCommand(TelegramBot telegramBot, long chatId, BotLanguage language)
    {
        String message = switch (language)
        {
            case RUSSIAN -> Messages.RU_HELP;
            case ENGLISH -> Messages.EN_HELP;
            case CHINESE -> Messages.CN_HELP;
            case GERMAN -> Messages.DE_HELP;
        };
        telegramBot.executeMessage(createMessage(chatId, message), null);
    }

    public void langCommand(TelegramBot telegramBot, long chatId, BotLanguage language)
    {
        String message = switch (language)
        {
            case RUSSIAN -> Messages.SET_LANG_RU;
            case ENGLISH -> Messages.SET_LANG_EN;
            case CHINESE -> Messages.SET_LANG_CN;
            case GERMAN -> Messages.SET_LANG_DE;
        };
        telegramBot.executeMessage(createMessage(chatId, message), BotModel::getRowsForScreenButton);
    }

    public void mapCommand(TelegramBot telegramBot, long chatId, BotLanguage language)
    {
        String message = switch (language)
        {
            case RUSSIAN -> Messages.RU_NAME_OF_CITY;
            case ENGLISH -> Messages.EN_NAME_OF_CITY;
            case CHINESE -> Messages.CN_NAME_OF_CITY;
            case GERMAN -> Messages.DE_NAME_OF_CITY;
        };
        telegramBot.executeMessage(createMessage(chatId, message), null);
    }

    public void defaultCommand(TelegramBot telegramBot, long chatId, BotLanguage language)
    {
        String message = switch (language)
        {
            case RUSSIAN -> Messages.RU_UNSUPPORTED_COMMAND;
            case ENGLISH -> Messages.EN_UNSUPPORTED_COMMAND;
            case CHINESE -> Messages.CN_UNSUPPORTED_COMMAND;
            case GERMAN -> Messages.DE_UNSUPPORTED_COMMAND;
        };
        telegramBot.executeMessage(createMessage(chatId, message), null);
    }
}