package ru.weather.bot.weatherbot.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
import ru.weather.bot.weatherbot.config.BotConfig;
import ru.weather.bot.weatherbot.enums.BotCommand;
import ru.weather.bot.weatherbot.enums.BotLanguage;
import ru.weather.bot.weatherbot.json.WeatherMapper;
import ru.weather.bot.weatherbot.models.BotModel;
import ru.weather.bot.weatherbot.models.Messages;

import java.io.File;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot
{
    private final BotConfig botConfig;
    private final WeatherMapper weatherMapper;
    private BotLanguage botLanguage;
    private BotCommand botCommand;

    @Autowired
    public TelegramBot(BotConfig botConfig, WeatherMapper weatherMapper) throws TelegramApiException
    {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.weatherMapper = weatherMapper;
        botLanguage = BotLanguage.ENGLISH;
        execute(new SetMyCommands(BotModel.commandListForBotMenu(), new BotCommandScopeDefault(), null));
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

            switch (message)
            {
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    botCommand = BotCommand.START;
                    break;
                case "/help":
                    helpCommand(chatId);
                    botCommand = BotCommand.HELP;
                    break;
                case "/lang":
                    langCommand(chatId);
                    botCommand = BotCommand.LANG;
                    break;
                case "/map":
                    mapCommand(chatId);
                    botCommand = BotCommand.MAP;
                    break;
                default:
                    if (botCommand.getCommand().equals(BotCommand.MAP.getCommand())){
                        File photoMap = weatherMapper.fetchWeatherMap(message, botLanguage);
                        if (photoMap == null){
                            executeMessage(chatId, switch (botLanguage)
                            {
                                case RUSSIAN -> Messages.RU_CITY_INPUT_ERROR;
                                case ENGLISH -> Messages.EN_CITY_INPUT_ERROR;
                                case CHINESE -> Messages.CN_CITY_INPUT_ERROR;
                                case GERMAN -> Messages.DE_CITY_INPUT_ERROR;
                            });
                        }
                        else{
                            try {
                                SendPhoto sendPhoto = new SendPhoto();
                                sendPhoto.setChatId(chatId);
                                sendPhoto.setPhoto(new InputFile(photoMap));
                                execute(sendPhoto);
                                photoMap.delete();
                                botCommand = BotCommand.DEFAULT;
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        if (message.contains("/"))
                            defaultCommand(chatId);
                        else
                        {
                            String weather = weatherMapper.weatherDispatch(message.toLowerCase(), botLanguage);
                            if (weather == null || weather.isEmpty())
                            {
                                executeMessage(chatId, switch (botLanguage)
                                {
                                    case RUSSIAN -> Messages.RU_CITY_INPUT_ERROR;
                                    case ENGLISH -> Messages.EN_CITY_INPUT_ERROR;
                                    case CHINESE -> Messages.CN_CITY_INPUT_ERROR;
                                    case GERMAN -> Messages.DE_CITY_INPUT_ERROR;
                                });
                            } else
                            {
                                botConfig.setMessageId(update.getMessage().getMessageId() + 1);
                                weatherMapper.getWeatherConfig().setCityName(message.toLowerCase());
                                sendMessageWithScreenButton(chatId, weather);
                            }
                        }
                    }
            }
        } else if (update.hasCallbackQuery())
        {
            String newMessage;
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            EditMessageText text = new EditMessageText();
            text.setChatId(chatId);
            text.setMessageId(messageId);
            switch (callbackData)
            {
                case "RuLang":
                    if (botLanguage == BotLanguage.RUSSIAN)
                        executeMessage(Messages.RU_THE_LANGUAGE_IS_ALREADY_THERE, text);
                    else
                    {
                        newMessage = Messages.RU_FURTHER_COMMUNICATION;
                        executeMessage(newMessage, text);
                        botLanguage = BotLanguage.RUSSIAN;
                    }
                    break;
                case "EnLang":
                    if (botLanguage == BotLanguage.ENGLISH)
                        executeMessage(Messages.EN_THE_LANGUAGE_IS_ALREADY_THERE, text);
                    else
                    {
                        newMessage = Messages.EN_FURTHER_COMMUNICATION;
                        executeMessage(newMessage, text);
                        botLanguage = BotLanguage.ENGLISH;
                    }
                    break;
                case "CnLang":
                    if (botLanguage == BotLanguage.CHINESE)
                        executeMessage(Messages.CN_THE_LANGUAGE_IS_ALREADY_THERE, text);
                    else
                    {
                        newMessage = Messages.CN_FURTHER_COMMUNICATION;
                        executeMessage(newMessage, text);
                        botLanguage = BotLanguage.CHINESE;
                    }
                    break;
                case "DeLang":
                    if (botLanguage == BotLanguage.GERMAN)
                        executeMessage(Messages.DE_THE_LANGUAGE_IS_ALREADY_THERE, text);
                    else
                    {
                        newMessage = Messages.DE_FURTHER_COMMUNICATION;
                        executeMessage(newMessage, text);
                        botLanguage = BotLanguage.GERMAN;
                    }
                    break;
                case "DetailedWeather":
                    String message = weatherMapper.getWeatherConfig().getCityName();
                    String unsuccessfulEvent = switch (botLanguage)
                    {
                        case RUSSIAN -> Messages.RU_UNSUCCESSFUL_EVENT_HANDLING;
                        case ENGLISH -> Messages.EN_UNSUCCESSFUL_EVENT_HANDLING;
                        case CHINESE -> Messages.CN_UNSUCCESSFUL_EVENT_HANDLING;
                        case GERMAN -> Messages.DE_UNSUCCESSFUL_EVENT_HANDLING;
                    };
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (message == null || message.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else executeMessage(weatherMapper.detailedWeatherForecast(message, botLanguage), text);
                    return;
                default:
                    defaultCommand(chatId);
            }
            if (botCommand != null && botCommand.getCommand().equals(BotCommand.START.getCommand()))
                helpCommand(chatId);
        }
    }

    private void executeMessage(String text, @NotNull EditMessageText messageText)
    {
        messageText.setText(text);
        messageText.setParseMode(ParseMode.HTML);
        try
        {
            execute(messageText);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void executeMessage(long chatId, String text)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode(ParseMode.HTML);
        try
        {
            execute(sendMessage);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageWithScreenButton(long chatId, String text)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode(ParseMode.HTML);

        var markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = BotModel.getButtonForDetailedWeather(botLanguage);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        try
        {
            execute(sendMessage);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void startCommand(long chatId, String name)
    {
        String message = "Hi, " + name + ", glad to meet you \uD83D\uDC4B\n\n" +
                "❗✋ Before you work with me further, choose the right language to communicate in. ✋❗";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = BotModel.getRowsForScreenButton();
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        try
        {
            execute(sendMessage);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void helpCommand(long chatId)
    {
        String message = switch (botLanguage)
        {
            case RUSSIAN -> Messages.RU_HELP;
            case ENGLISH -> Messages.EN_HELP;
            case CHINESE -> Messages.CN_HELP;
            case GERMAN -> Messages.DE_HELP;
        };
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

    public void langCommand(long chatId)
    {
        String message = switch (botLanguage)
        {
            case RUSSIAN -> Messages.SET_LANG_RU;
            case ENGLISH -> Messages.SET_LANG_EN;
            case CHINESE -> Messages.SET_LANG_CN;
            case GERMAN -> Messages.SET_LANG_DE;
        };
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = BotModel.getRowsForScreenButton();
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
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
        String message = switch (botLanguage)
        {
            case RUSSIAN -> Messages.RU_UNSUPPORTED_COMMAND;
            case ENGLISH -> Messages.EN_UNSUPPORTED_COMMAND;
            case CHINESE -> Messages.CN_UNSUPPORTED_COMMAND;
            case GERMAN -> Messages.DE_UNSUPPORTED_COMMAND;
        };
        executeMessage(chatId, message);
    }

    private void mapCommand(long chatId){
        String message = switch (botLanguage)
        {
            case RUSSIAN -> Messages.RU_NAME_OF_CITY;
            case ENGLISH -> Messages.EN_NAME_OF_CITY;
            case CHINESE -> Messages.CN_NAME_OF_CITY;
            case GERMAN -> Messages.DE_NAME_OF_CITY;
    };
        executeMessage(chatId, message);
    }
}