package ru.weather.bot.weatherbot.service;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
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
                default:
                    if (message.contains("/"))
                        defaultCommand(chatId);
                    else
                    {
                        String weather, input_error = switch (botLanguage)
                        {
                            case RUSSIAN -> Messages.RU_CITY_INPUT_ERROR;
                            case ENGLISH -> Messages.EN_CITY_INPUT_ERROR;
                            case CHINESE -> Messages.CN_CITY_INPUT_ERROR;
                            case GERMAN -> Messages.DE_CITY_INPUT_ERROR;
                        };
                        if (message.matches("^.*\\s\\d{1,2}$"))
                        {
                            weather = weatherMapper.weatherForecastDispatch(message.toLowerCase(), botLanguage);
                            if (weather == null || weather.isEmpty())
                                executeMessage(chatId, input_error);
                            else
                            {
                                String[] city_days = weatherMapper.splitSpace(message.toLowerCase());
                                botConfig.setMessageId(update.getMessage().getMessageId() + 1);
                                weatherMapper.getWeatherConfig().setWeatherMessage(weather);
                                weatherMapper.getWeatherConfig().setQuantityDays(Integer.parseInt(city_days[1]));
                                weatherMapper.getWeatherConfig().setCityName(city_days[0]);
                                sendMessWeatherForecast(chatId, Integer.parseInt(city_days[1]), weather);
                            }
                        }
                        else
                        {
                            weather = weatherMapper.weatherDispatch(message.toLowerCase(), botLanguage);
                            if (weather == null || weather.isEmpty())
                                executeMessage(chatId, input_error);
                            else
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

            String unsuccessfulEvent = switch (botLanguage)
            {
                case RUSSIAN -> Messages.RU_UNSUCCESSFUL_EVENT_HANDLING;
                case ENGLISH -> Messages.EN_UNSUCCESSFUL_EVENT_HANDLING;
                case CHINESE -> Messages.CN_UNSUCCESSFUL_EVENT_HANDLING;
                case GERMAN -> Messages.DE_UNSUCCESSFUL_EVENT_HANDLING;
            };
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
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (message == null || message.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else executeMessage(weatherMapper.detailedWeather(message, botLanguage), text);
                    return;
                case "WF_1":
                    String cityName1 = weatherMapper.getWeatherConfig().getCityName();
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (cityName1 == null || cityName1.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else execMessage(weatherMapper.weatherForecastDay(cityName1, 1, botLanguage), text);
                    return;
                case "WF_2":
                    String cityName2 = weatherMapper.getWeatherConfig().getCityName();
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (cityName2 == null || cityName2.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else execMessage(weatherMapper.weatherForecastDay(cityName2, 2, botLanguage), text);
                    return;
                case "WF_3":
                    String cityName3 = weatherMapper.getWeatherConfig().getCityName();
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (cityName3 == null || cityName3.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else execMessage(weatherMapper.weatherForecastDay(cityName3, 3, botLanguage), text);
                    return;
                case "WF_4":
                    String cityName4 = weatherMapper.getWeatherConfig().getCityName();
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (cityName4 == null || cityName4.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else execMessage(weatherMapper.weatherForecastDay(cityName4, 4, botLanguage), text);
                    return;
                case "WF_5":
                    String cityName5 = weatherMapper.getWeatherConfig().getCityName();
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (cityName5 == null || cityName5.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else execMessage(weatherMapper.weatherForecastDay(cityName5, 5, botLanguage), text);
                    return;
                case "Back":
                    String weatherMessage = weatherMapper.getWeatherConfig().getWeatherMessage();
                    if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue() ||
                            (weatherMessage == null || weatherMessage.isEmpty())))
                        executeMessage(chatId, unsuccessfulEvent);
                    else editMessageWF(weatherMessage, text);
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

    private void execMessage(String text, @NotNull EditMessageText messageText)
    {
        messageText.setText(text);
        messageText.setParseMode(ParseMode.HTML);
        var markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = BotModel.buttonWF(botLanguage);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        try
        {
            execute(messageText);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void editMessageWF(String text, @NotNull EditMessageText messageText)
    {
        messageText.setText(text);
        messageText.setParseMode(ParseMode.HTML);
        var markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = BotModel.getButtonsForecastWeather(weatherMapper.getWeatherConfig().getQuantityDays());
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
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

    public void sendMessWeatherForecast(long chatId, @Max(value = 10) int countDays, String text)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode(ParseMode.HTML);
        var markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = BotModel.getButtonsForecastWeather(countDays);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
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
}