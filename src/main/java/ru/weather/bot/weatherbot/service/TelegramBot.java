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
            botConfig.setMessageId(update.getMessage().getMessageId() + 1);
            if (botCommand == null && !(message.equals(BotCommand.START.getCommand()))){
                executeMessage(chatId, Messages.EN_UNSUCCESSFUL_RELOAD_EVENT_HANDLING, null);
                return;
            }

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
//                    if (botCommand == null){
//                        executeMessage(chatId, Messages.EN_UNSUCCESSFUL_RELOAD_EVENT_HANDLING, null);
//                    }
                    if (botCommand.getCommand().equals(BotCommand.MAP.getCommand())){
                        File photoMap = weatherMapper.fetchWeatherMap(message, botLanguage);
                        if (photoMap == null){
                            executeMessage(chatId, switch (botLanguage)
                            {
                                case RUSSIAN -> Messages.RU_CITY_INPUT_ERROR;
                                case ENGLISH -> Messages.EN_CITY_INPUT_ERROR;
                                case CHINESE -> Messages.CN_CITY_INPUT_ERROR;
                                case GERMAN -> Messages.DE_CITY_INPUT_ERROR;
                            }, null);
                            botCommand = BotCommand.DEFAULT;
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
                    else {
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
                                    executeMessage(chatId, input_error, null);
                                else
                                {
                                    String[] city_days = weatherMapper.splitSpace(message.toLowerCase());
                                    weatherMapper.getWeatherConfig().setWeatherMessage(weather);
                                    weatherMapper.getWeatherConfig().setQuantityDays(Integer.parseInt(city_days[1]));
                                    weatherMapper.getWeatherConfig().setCityName(city_days[0]);
                                    executeMessage(chatId, weather, () -> BotModel.getButtonsForecastWeather(weatherMapper.getWeatherConfig().getQuantityDays()));
                                }
                            }
                            else
                            {
                                weather = weatherMapper.weatherDispatch(message.toLowerCase(), botLanguage);
                                if (weather == null || weather.isEmpty())
                                    executeMessage(chatId, input_error, null);
                                else
                                {
                                    weatherMapper.getWeatherConfig().setCityName(message.toLowerCase());
                                    executeMessage(chatId, weather, () -> BotModel.getButtonForDetailedWeather(botLanguage));
                                }
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

            if (botConfig.getMessageId() == null || (messageId.intValue() != botConfig.getMessageId().intValue()))
            {
                executeMessage(chatId, unsuccessfulEvent, null);
                return;
            }
            switch (callbackData)
            {
                case "RuLang":
                    if (botLanguage == BotLanguage.RUSSIAN)
                        execMessage(Messages.RU_THE_LANGUAGE_IS_ALREADY_THERE, text, null);
                    else
                    {
                        newMessage = Messages.RU_FURTHER_COMMUNICATION;
                        execMessage(newMessage, text, null);
                        botLanguage = BotLanguage.RUSSIAN;
                    }
                    break;
                case "EnLang":
                    if (botLanguage == BotLanguage.ENGLISH)
                        execMessage(Messages.EN_THE_LANGUAGE_IS_ALREADY_THERE, text, null);
                    else
                    {
                        newMessage = Messages.EN_FURTHER_COMMUNICATION;
                        execMessage(newMessage, text, null);
                        botLanguage = BotLanguage.ENGLISH;
                    }
                    break;
                case "CnLang":
                    if (botLanguage == BotLanguage.CHINESE)
                        execMessage(Messages.CN_THE_LANGUAGE_IS_ALREADY_THERE, text, null);
                    else
                    {
                        newMessage = Messages.CN_FURTHER_COMMUNICATION;
                        execMessage(newMessage, text, null);
                        botLanguage = BotLanguage.CHINESE;
                    }
                    break;
                case "DeLang":
                    if (botLanguage == BotLanguage.GERMAN)
                        execMessage(Messages.DE_THE_LANGUAGE_IS_ALREADY_THERE, text, null);
                    else
                    {
                        newMessage = Messages.DE_FURTHER_COMMUNICATION;
                        execMessage(newMessage, text, null);
                        botLanguage = BotLanguage.GERMAN;
                    }
                    break;
                case "DetailedWeather":
                    String message = weatherMapper.getWeatherConfig().getCityName();
                    execMessage(weatherMapper.detailedWeather(message, botLanguage), text, null);
                    return;
                case "WF_1":
                    String cityName1 = weatherMapper.getWeatherConfig().getCityName();
                    execMessage(weatherMapper.weatherForecastDay(cityName1, 1, botLanguage), text, () -> BotModel.buttonWF(botLanguage));
                    return;
                case "WF_2":
                    String cityName2 = weatherMapper.getWeatherConfig().getCityName();
                    execMessage(weatherMapper.weatherForecastDay(cityName2, 2, botLanguage), text, () -> BotModel.buttonWF(botLanguage));
                    return;
                case "WF_3":
                    String cityName3 = weatherMapper.getWeatherConfig().getCityName();
                    execMessage(weatherMapper.weatherForecastDay(cityName3, 3, botLanguage), text, () -> BotModel.buttonWF(botLanguage));
                    return;
                case "WF_4":
                    String cityName4 = weatherMapper.getWeatherConfig().getCityName();
                    execMessage(weatherMapper.weatherForecastDay(cityName4, 4, botLanguage), text, () -> BotModel.buttonWF(botLanguage));
                    return;
                case "WF_5":
                    String cityName5 = weatherMapper.getWeatherConfig().getCityName();
                    execMessage(weatherMapper.weatherForecastDay(cityName5, 5, botLanguage), text, () -> BotModel.buttonWF(botLanguage));
                    return;
                case "Back":
                    String weatherMessage = weatherMapper.getWeatherConfig().getWeatherMessage();
                    execMessage(weatherMessage, text, () -> BotModel.getButtonsForecastWeather(weatherMapper.getWeatherConfig().getQuantityDays()));
                    return;
                default:
                    defaultCommand(chatId);
            }
            if (botCommand != null && botCommand.getCommand().equals(BotCommand.START.getCommand()))
                helpCommand(chatId);
        }
    }

    private void execMessage(String text, @NotNull EditMessageText messageText, EditMessage message)
    {
        messageText.setText(text);
        messageText.setParseMode(ParseMode.HTML);
        if (message != null)
        {
            var markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = message.getButton();
            markup.setKeyboard(rows);
            messageText.setReplyMarkup(markup);
        }
        try
        {
            execute(messageText);
        } catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void executeMessage(long chatId, String text, EditMessage message)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode(ParseMode.HTML);
        if (message != null)
        {
            var markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = message.getButton();
            markup.setKeyboard(rows);
            sendMessage.setReplyMarkup(markup);
        }
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
        executeMessage(chatId, message, BotModel::getRowsForScreenButton);
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
        executeMessage(chatId, message, null);
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
        executeMessage(chatId, message, BotModel::getRowsForScreenButton);
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
        executeMessage(chatId, message, null);
    }

    private void mapCommand(long chatId){
        String message = switch (botLanguage)
        {
            case RUSSIAN -> Messages.RU_NAME_OF_CITY;
            case ENGLISH -> Messages.EN_NAME_OF_CITY;
            case CHINESE -> Messages.CN_NAME_OF_CITY;
            case GERMAN -> Messages.DE_NAME_OF_CITY;
        };
        executeMessage(chatId, message, null);
    }
}