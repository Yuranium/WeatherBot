package ru.weather.bot.weatherbot.service;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.weather.bot.weatherbot.Observer;
import ru.weather.bot.weatherbot.Subject;
import ru.weather.bot.weatherbot.config.BotConfig;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.json.ProcessingData;
import ru.weather.bot.weatherbot.json.ReceiveData;
import ru.weather.bot.weatherbot.json.WeatherMapper;
import ru.weather.bot.weatherbot.models.BotModel;
import ru.weather.bot.weatherbot.models.Messages;

import java.io.File;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

import static ru.weather.bot.weatherbot.service.MessageSend.createMessage;

@Service
public class TelegramBot extends TelegramLongPollingBot implements Subject
{
    private final BotConfig botConfig;
    private BotLanguage botLanguage;
    private BotCommand botCommand;
    private final List<Observer> observers;
    private final IncomingText incomingText;
    private final IncomingCallBack incomingCallBack;

    @Autowired
    public TelegramBot(BotConfig botConfig, List<Observer> observers, IncomingText incomingText, IncomingCallBack incomingCallBack) throws TelegramApiException
    {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.observers = observers;
        this.incomingText = incomingText;
        this.incomingCallBack = incomingCallBack;
        botLanguage = BotLanguage.ENGLISH;
        execute(new SetMyCommands(BotModel.commandListForBotMenu(), new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername()
    {
        return botConfig.getBotName();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            botConfig.setMessageId(update.getMessage().getMessageId() + 1);
            sendApiMethod(incomingText.sendler(update));
        } else if (update.hasCallbackQuery())
        {
            String callBack = update.getCallbackQuery().getData();
            switch (callBack)
            {
                case "RuLang":
                    sendApiMethod(languageSwitching(update, BotLanguage.RUSSIAN));
                    break;
                case "EnLang":
                    sendApiMethod(languageSwitching(update, BotLanguage.ENGLISH));
                    break;
                case "CnLang":
                    sendApiMethod(languageSwitching(update, BotLanguage.CHINESE));
                    break;
                case "DeLang":
                    sendApiMethod(languageSwitching(update, BotLanguage.GERMAN));
                    break;
                default:
                    sendApiMethod(incomingCallBack.sendler(update));
            }
        }
    }

    @SneakyThrows
    public void executePhoto(String message, long chatId, String error)
    {
        File photoMap = receiveData.fetchWeatherMap(message, botLanguage);
        botCommand = BotCommand.DEFAULT;
        if (photoMap == null)
            executeMessage(createMessage(chatId, error), null);
        else
        {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(photoMap));
            execute(sendPhoto);
            photoMap.delete();
            executeMessage(createMessage(chatId, Messages.infoWeatherWithMap(botLanguage, processingData.convertCityNameCorrectly(message))), null);
        }
    }

    private BotApiMethod<?> languageSwitching(Update update, BotLanguage language)
    {
        EditMessageText editText = EditMessageText.builder()
                .chatId(update.getMessage().getChatId())
                .messageId(update.getMessage().getMessageId())
                .replyMarkup(new InlineKeyboardMarkup(BotModel.getBackButton()))
                .parseMode(ParseMode.HTML)
                .build();
        if (botLanguage == language)
        {
            editText.setText(switch (language)
            {
                case RUSSIAN -> Messages.RU_THE_LANGUAGE_IS_ALREADY_THERE;
                case ENGLISH -> Messages.EN_THE_LANGUAGE_IS_ALREADY_THERE;
                case CHINESE -> Messages.CN_THE_LANGUAGE_IS_ALREADY_THERE;
                case GERMAN -> Messages.DE_THE_LANGUAGE_IS_ALREADY_THERE;
            });
            return editText;
        }
        else
        {
            editText.setText(switch (language)
            {
                case RUSSIAN -> Messages.RU_FURTHER_COMMUNICATION;
                case ENGLISH -> Messages.EN_FURTHER_COMMUNICATION;
                case CHINESE -> Messages.CN_FURTHER_COMMUNICATION;
                case GERMAN -> Messages.DE_FURTHER_COMMUNICATION;
            });
            botLanguage = language;
            notifyObservers();
            BotModel.language = botLanguage;
            return editText;
        }
    }

    @Override
    public void notifyObservers()
    {
        observers.forEach(obs -> obs.update(botLanguage));
    }
}